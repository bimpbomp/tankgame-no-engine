package com.example.helloworld.systems.physics;

import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class PhysicsSystem extends GameSystem {
    private World world;
    private final Vec2 gravity = new Vec2(0f, 0f);
    private final int velocityIterations = 6;
    private final int positionIterations = 2;

    public PhysicsSystem(Coordinator coordinator){
        super(coordinator);
        signature.set(Component.getType(PhysicsComponent.class));
        world = new World(gravity);
    }

    @Override
    public void init() {

    }

    public void update(float delta){
        world.step(delta, velocityIterations, positionIterations);

        for (Entity entity : entities){
            PhysicsComponent physicsComponent = (PhysicsComponent) coordinator.getComponent(entity, PhysicsComponent.class);

            entity.position = physicsComponent.body.getPosition();
            entity.angle = physicsComponent.body.getAngle();

            Log.d("movement", "ang: " + physicsComponent.body.getAngle() + " angvel: " + physicsComponent.body.getAngularVelocity());

        }
    }

    @Override
    public void addEntity(Entity entity) {
        super.addEntity(entity);
        PhysicsComponent physicsComponent = (PhysicsComponent) coordinator.getComponent(entity, PhysicsComponent.class);
        physicsComponent.body = world.createBody(physicsComponent.bodyDef);
        physicsComponent.body.createFixture(physicsComponent.fixtureDef);
    }

    @Override
    public void removeEntity(Entity entity) {
        if (entities.contains(entity)) {
            PhysicsComponent physicsComponent = (PhysicsComponent) coordinator.getComponent(entity, PhysicsComponent.class);
            world.destroyBody(physicsComponent.body);
        }

        super.removeEntity(entity);
    }
}
