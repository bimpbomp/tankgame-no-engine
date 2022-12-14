package com.example.helloworld.systems.level;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.CoordinateSystem;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.systems.physics.PhysicsBodyGenerator;
import com.example.helloworld.systems.physics.PhysicsSystem;
import com.example.helloworld.systems.render.PolygonFactory;
import org.jbox2d.common.Vec2;

import java.util.Collections;

public class WorldObjectGenerator {
    public static Entity generateWall(Coordinator coordinator, Vec2 position, int width, int height, int zOrder){
        Entity entity = coordinator.createEntity();
        entity.position = position;
        Renderable renderablePolygon = PolygonFactory.generateRectangle(width, height, CoordinateSystem.WORLD);
        renderablePolygon.zOrder = zOrder;
        renderablePolygon.color = Color.BLACK;
        coordinator.addComponent(entity, renderablePolygon);
        PhysicsComponent physicsComponent = PhysicsBodyGenerator.createPhysicsBody(entity.position, false, width, height);
        coordinator.addComponent(entity, physicsComponent);
        Log.d("Loading", entity.id + " (wall): " + entity.signature.toString());

        return entity;
    }

    public static Entity generateTank(Coordinator coordinator, Vec2 position, int width, int height, int zOrder, int color){
        Entity entity = coordinator.createEntity();
        entity.position = position;
        Renderable renderablePolygon = PolygonFactory.generateRectangle(width, height, CoordinateSystem.WORLD);
        renderablePolygon.zOrder = zOrder;
        renderablePolygon.color = color;
        coordinator.addComponent(entity, renderablePolygon);
        PhysicsComponent physicsComponent = PhysicsBodyGenerator.createPhysicsBody(entity.position, true, width, height);
        coordinator.addComponent(entity, physicsComponent);

        TankInput tankInput = new TankInput();
        coordinator.addComponent(entity, tankInput);

        Log.d("Loading", entity.id + " (tank): " + entity.signature.toString());
        return entity;
    }
}
