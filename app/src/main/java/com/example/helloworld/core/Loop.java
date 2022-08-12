package com.example.helloworld.core;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.WorldObjectGenerator;
import com.example.helloworld.components.Ui;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.android.GameSurface;
import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.core.android.EventLogger;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.Signature;
import com.example.helloworld.systems.PhysicsSystem;
import com.example.helloworld.systems.TankMovementSystem;
import com.example.helloworld.systems.input.GameInputSystem;
import com.example.helloworld.systems.render.PolygonFactory;
import com.example.helloworld.systems.render.RenderSystem;
import com.example.helloworld.systems.ViewportSystem;
import com.example.helloworld.systems.ui.UiSystem;
import org.jbox2d.common.Vec2;

// Loop taken from: https://dewitters.com/dewitters-gameloop/
public class Loop implements Runnable {
    private Coordinator coordinator;
    private RenderSystem renderSystem;
    private PhysicsSystem physicsSystem;
    private ViewportSystem viewportSystem;
    private GameInputSystem gameInputSystem;
    private UiSystem uiSystem;
    private TankMovementSystem tankMovementSystem;
    private EventLogger eventLogger;
    private boolean gameIsRunning;

    public Loop() {
    }

    public void initialiseGame(GameSurface gameSurface) {
        //eventLogger = new EventLogger();
        coordinator = new Coordinator();

        coordinator.registerComponentType(Component.getType(PhysicsBody.class));
        Log.d("Loading", "PhysicsBody id: " + Component.getType(PhysicsBody.class));
        coordinator.registerComponentType(Component.getType(Renderable.class));
        Log.d("Loading", "Renderable id: " + Component.getType(Renderable.class));
        coordinator.registerComponentType(Component.getType(Viewport.class));
        Log.d("Loading", "Viewport id: " + Component.getType(Viewport.class));
        coordinator.registerComponentType(Component.getType(Ui.class));
        Log.d("Loading", "Ui id: " + Component.getType(Ui.class));
        coordinator.registerComponentType(Component.getType(TankInput.class));
        Log.d("Loading", "UiControllable id: " + Component.getType(TankInput.class));

        {
            renderSystem = new RenderSystem(coordinator, gameSurface);
            coordinator.registerSystem(renderSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(Renderable.class));
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

        {
            gameInputSystem = new GameInputSystem(coordinator);
            coordinator.registerSystem(gameInputSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(Ui.class));
            gameInputSystem.setSignature(signature);
        }

        {
            uiSystem = new UiSystem(coordinator);
            coordinator.registerSystem(uiSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(Ui.class));
            uiSystem.setSignature(signature);
        }

        {
            tankMovementSystem = new TankMovementSystem(coordinator);
            coordinator.registerSystem(tankMovementSystem);
            Signature signature = new Signature();
            signature.set(Component.getType(TankInput.class));
            signature.set(Component.getType(PhysicsBody.class));
            tankMovementSystem.setSignature(signature);
        }

        // player
        {
            Entity entity = coordinator.createEntity();
            entity.position = new Vec2(10, 10);
            int width = 1;
            int height = 1;
            Renderable renderablePolygon = PolygonFactory.generateRectangle(width, height, CoordinateSystem.WORLD);
            renderablePolygon.zOrder = 99;
            renderablePolygon.color = Color.GREEN;
            coordinator.addComponent(entity, renderablePolygon);
            PhysicsBody physicsBody = physicsSystem.createPhysicsBody(entity.position, true, width, height);
            coordinator.addComponent(entity, physicsBody);

            TankInput tankInput = new TankInput();
            coordinator.addComponent(entity, tankInput);

            coordinator.setPlayer(entity);
            uiSystem.setControlledTankInput(tankInput);
            Log.d("Loading", entity.id + "(player): " + entity.signature.toString());
        }

        // generate walls
        WorldObjectGenerator.generateWall(coordinator, physicsSystem, new Vec2(10, 13), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, physicsSystem, new Vec2(10, 7), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, physicsSystem, new Vec2(7, 10), 1, 4, 99);
        WorldObjectGenerator.generateWall(coordinator, physicsSystem, new Vec2(13, 10), 1, 4, 99);

        {
            Entity entity = coordinator.createEntity();
            entity.position = new Vec2(0, 0);
            Viewport viewport = new Viewport();
            viewport.entityFocussedOn = coordinator.getPlayer();
            Log.d("Loading", "Screen width " + gameSurface.getWidth() + " height " + gameSurface.getHeight());
            viewport.width = gameSurface.getWidth();
            viewport.height = gameSurface.getHeight();
            coordinator.addComponent(entity, viewport);
            renderSystem.setViewport(entity);

            coordinator.setPlayerViewport(entity);
            Log.d("Loading", entity.id + ": " + entity.signature.toString());
        }
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
                updateGame(1f / TICKS_PER_SECOND);

                nextGameTick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float) (System.currentTimeMillis() + SKIP_TICKS - nextGameTick) / (float) SKIP_TICKS;
            renderSystem.update(interpolation);
        }
    }

    public void updateGame(float delta){
        gameInputSystem.update(delta);
        uiSystem.update(delta);
        tankMovementSystem.update(delta);
        physicsSystem.update(delta);
        viewportSystem.update(delta);
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }
}
