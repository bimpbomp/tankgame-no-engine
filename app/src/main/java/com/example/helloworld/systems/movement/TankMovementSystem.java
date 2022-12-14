package com.example.helloworld.systems.movement;

import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import org.jbox2d.common.Vec2;

public class TankMovementSystem extends GameSystem {
    public TankMovementSystem(Coordinator coordinator) {
        super(coordinator);
        signature.set(Component.getType(TankInput.class));
        signature.set(Component.getType(PhysicsComponent.class));
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities){
            TankInput tankInput = (TankInput) coordinator.getComponent(entity, TankInput.class);
            PhysicsComponent physicsComponent = (PhysicsComponent) coordinator.getComponent(entity, PhysicsComponent.class);

            Vec2 force = tankInput.movementVector.mul(1f);
            if (physicsComponent.body.m_linearVelocity.length() < 10f)
                physicsComponent.body.applyForceToCenter(force);
        }
    }
}
