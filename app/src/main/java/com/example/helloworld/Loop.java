package com.example.helloworld;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.RenderableCircle;
import com.example.helloworld.components.renderable.RenderablePolygon;
import com.example.helloworld.components.Transform;
import com.example.helloworld.components.renderable.RenderableSprite;
import com.example.helloworld.components.renderable.RenderableText;
import com.example.helloworld.ecs.*;
import com.example.helloworld.old.InputManager;
import com.example.helloworld.systems.PhysicsSystem;
import com.example.helloworld.systems.RenderSystem;
import com.example.helloworld.systems.ViewportSystem;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.Arrays;

// Loop taken from: https://dewitters.com/dewitters-gameloop/
public class Loop implements Runnable {
    private InputManager inputManager;
    private Coordinator coordinator;
    private RenderSystem renderSystem;
    private PhysicsSystem physicsSystem;
    private ViewportSystem viewportSystem;
    private boolean gameIsRunning;

    public Loop() {
    }

    public void initialiseGame(GameSurface gameSurface) {
        inputManager = new InputManager();
        coordinator = new Coordinator();

        coordinator.registerComponentType(Component.getType(Transform.class));
        coordinator.registerComponentType(Component.getType(PhysicsBody.class));
        coordinator.registerComponentType(Component.getType(RenderablePolygon.class));
        coordinator.registerComponentType(Component.getType(RenderableCircle.class));
        coordinator.registerComponentType(Component.getType(RenderableText.class));
        coordinator.registerComponentType(Component.getType(RenderableSprite.class));
        coordinator.registerComponentType(Component.getType(Viewport.class));

        {
            renderSystem = new RenderSystem(coordinator, gameSurface);
            coordinator.registerSystem(renderSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(RenderablePolygon.class));
            renderSystem.setSignature(signature);
        }

        {
            physicsSystem = new PhysicsSystem(coordinator);
            coordinator.registerSystem(physicsSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(PhysicsBody.class));
            physicsSystem.setSignature(signature);
        }

        {
            viewportSystem = new ViewportSystem(coordinator);
            coordinator.registerSystem(viewportSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(Viewport.class));
            viewportSystem.setSignature(signature);
        }

        Entity player;
        {
            Entity entity = coordinator.createEntity();
            player = entity;
            Transform transform = new Transform();
            transform.position = new Vec2(100, 100);
            transform.scale = 100f;
            coordinator.addComponent(entity, transform);
            RenderablePolygon renderablePolygon = PolygonFactory.generateRectangle(100, 100);
            renderablePolygon.zOrder = 99;
            renderablePolygon.color = Color.GREEN;
            coordinator.addComponent(entity, renderablePolygon);
            PhysicsBody physicsBody = physicsSystem.createPhysicsBody(transform);
            coordinator.addComponent(entity, physicsBody);
        }

        {
            Entity entity = coordinator.createEntity();
            Transform transform = new Transform();
            transform.position = new Vec2(400, 100);
            transform.scale = 200f;
            coordinator.addComponent(entity, transform);
            RenderablePolygon renderablePolygon = PolygonFactory.generateTriangle(200, 200);
            renderablePolygon.zOrder = 100;
            renderablePolygon.color = Color.RED;
            coordinator.addComponent(entity, renderablePolygon);
            PhysicsBody physicsBody = physicsSystem.createPhysicsBody(transform);
            coordinator.addComponent(entity, physicsBody);
        }

        {
            Entity entity = coordinator.createEntity();
            Transform transform = new Transform();
            transform.position = new Vec2(0, 0);
            coordinator.addComponent(entity, transform);
            Viewport viewport = new Viewport();
            viewport.entityFocussedOn = player;
            Log.d("Loading", "Screen width " + gameSurface.getWidth() + " height " + gameSurface.getHeight());
            viewport.width = gameSurface.getWidth();
            viewport.height = gameSurface.getHeight();
            coordinator.addComponent(entity, viewport);
            renderSystem.setViewport(entity);
        }
    }

    public void updateGame(){
        //Point inputUnitVector = VectorUtils.getUnitVector(inputManager.pollDpadInput());
        float delta = 0.0f;
        //physicsSystem.update(delta);
        viewportSystem.update(delta);
    }

    @Override
    public void run() {
        int TICKS_PER_SECOND = 25;
        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 5;

        long nextGameTick = System.currentTimeMillis();
        int loops;
        float interpolation;

        Log.d("Loading", "Game loop starting");
        gameIsRunning = true;
        while (gameIsRunning) {
            loops = 0;

            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
                updateGame();

                nextGameTick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float) (System.currentTimeMillis() + SKIP_TICKS - nextGameTick) / (float) SKIP_TICKS;
            renderSystem.update(interpolation);
        }
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
