package com.example.helloworld;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.CoordinateSystem;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.systems.PhysicsSystem;
import com.example.helloworld.systems.render.PolygonFactory;
import org.jbox2d.common.Vec2;

public class WorldObjectGenerator {
    public static Entity generateWall(Coordinator coordinator, PhysicsSystem physicsSystem, Vec2 position, int width, int height, int zOrder){
        Entity entity = coordinator.createEntity();
        entity.position = position;
        Renderable renderablePolygon = PolygonFactory.generateRectangle(width, height, CoordinateSystem.WORLD);
        renderablePolygon.zOrder = zOrder;
        renderablePolygon.color = Color.BLACK;
        coordinator.addComponent(entity, renderablePolygon);
        PhysicsBody physicsBody = physicsSystem.createPhysicsBody(entity.position, false, width, height);
        coordinator.addComponent(entity, physicsBody);
        Log.d("Loading", entity.id + ": " + entity.signature.toString());

        return entity;
    }
}
