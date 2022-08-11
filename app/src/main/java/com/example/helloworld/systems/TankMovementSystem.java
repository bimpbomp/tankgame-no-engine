package com.example.helloworld.systems;

import android.util.Log;
import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import org.jbox2d.common.Vec2;

public class TankMovementSystem extends GameSystem {
    public TankMovementSystem(Coordinator coordinator) {
        super(coordinator);
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities){
            TankInput tankInput = (TankInput) coordinator.getComponent(entity, TankInput.class);
            PhysicsBody physicsBody = (PhysicsBody) coordinator.getComponent(entity, PhysicsBody.class);

            Vec2 force = tankInput.movementVector.mul(10f);
            if (force.x != 0f && force.y != 0f)
                Log.d("Physics", "Applying force: " + force);
            physicsBody.body.applyForceToCenter(force);
        }
    }
}
