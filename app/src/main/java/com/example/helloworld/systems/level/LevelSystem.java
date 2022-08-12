package com.example.helloworld.systems.level;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.CoordinateSystem;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.physics.PhysicsBodyGenerator;
import com.example.helloworld.systems.render.PolygonFactory;
import com.example.helloworld.systems.render.ViewportChangeEvent;
import org.jbox2d.common.Vec2;

public class LevelSystem extends GameSystem implements ISubscriber {
    private boolean needToLoad;
    private Publisher viewportEventPublisher;

    public LevelSystem(Coordinator coordinator) {
        super(coordinator);
        PublisherHub.getInstance().subscribe(GameEventType.LEVEL_CHANGE, this);
        needToLoad = false;
        this.viewportEventPublisher = PublisherHub.getInstance().createNewPublisher(GameEventType.VIEWPORT_CHANGE);
    }

    @Override
    public void update(float delta) {
        if (needToLoad){
            unloadCurrentLevel();
            loadNewLevel();
        }
    }

    @Override
    public void onNotify(GameEvent event) {
        needToLoad = true;
    }

    private void loadNewLevel(){
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
            PhysicsComponent physicsComponent = PhysicsBodyGenerator.createPhysicsBody(entity.position, true, width, height);
            coordinator.addComponent(entity, physicsComponent);

            TankInput tankInput = new TankInput();
            coordinator.addComponent(entity, tankInput);

            coordinator.setPlayer(entity);
            uiSystem.setControlledTankInput(tankInput);
            Log.d("Loading", entity.id + "(player): " + entity.signature.toString());
        }

        // player viewport
        {
            Entity entity = coordinator.createEntity();
            entity.position = new Vec2(0, 0);
            Viewport viewport = new Viewport();
            viewport.entityFocussedOn = coordinator.getPlayer();
            Log.d("Loading", "Screen width " + gameSurface.getWidth() + " height " + gameSurface.getHeight());
            viewport.width = gameSurface.getWidth();
            viewport.height = gameSurface.getHeight();
            coordinator.addComponent(entity, viewport);
            viewportEventPublisher.notify(new ViewportChangeEvent(entity));

            coordinator.setPlayerViewport(entity);
            Log.d("Loading", entity.id + ": " + entity.signature.toString());
        }

        // generate walls
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 13), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 7), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(7, 10), 1, 4, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(13, 10), 1, 4, 99);
    }

    private void unloadCurrentLevel(){
        Log.d("Loading", "Unloading current level...");
    }
}
