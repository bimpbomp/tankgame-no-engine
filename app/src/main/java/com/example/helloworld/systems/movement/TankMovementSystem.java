package com.example.helloworld.systems.movement;

import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.core.VectorUtils;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

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
            applyForce(entity);
        }
    }

    private void killOrthogonalVelocity(Body body){
        Vec2 localPoint = new Vec2();
        Vec2 velocity = body.getLinearVelocityFromLocalPoint(localPoint);

        Vec2 rightNormal = body.getWorldVector(new Vec2(1, 0));
        rightNormal = rightNormal.mul(Vec2.dot(velocity, rightNormal));
        body.setLinearVelocity(rightNormal);
    }

    private void applyForce(Entity entity){
        TankInput tankInput = (TankInput) coordinator.getComponent(entity, TankInput.class);
        PhysicsComponent physicsComponent = (PhysicsComponent) coordinator.getComponent(entity, PhysicsComponent.class);

        if (tankInput.movementVector.length() > 0) {
            // todo test the rotate function works
            Vec2 forwardVector = VectorUtils.rotate(new Vec2(0, -1), entity.angle);
            float angle = VectorUtils.angle(forwardVector, tankInput.movementVector);

            Vec2 leftForce = new Vec2(forwardVector);
            Vec2 rightForce = new Vec2(forwardVector);

            if (angle > -45 && angle < 45){
                // do nothing, go straight on
            } else if (angle <= -45 && angle > -135){
                // turn right
                rightForce = rightForce.mul(-1f);
            } else if (angle <= -135 || angle > 135){
                // reverse
                leftForce = leftForce.mul(-1f);
                rightForce = rightForce.mul(-1f);
            } else if (angle >= 45 && angle < 135){
                // turn left
                leftForce = leftForce.mul(-1f);
            } else {
                Log.d("movement", "shouldnt happen");
            }

            Vec2 leftTurningPoint = calculateLeftTurnPoint(forwardVector, entity.position, physicsComponent.width);
            Vec2 rightTurningPoint = calculateRightTurnPoint(forwardVector, entity.position, physicsComponent.width);

            physicsComponent.body.applyForce(leftForce, leftTurningPoint);
            physicsComponent.body.applyForce(rightForce, rightTurningPoint);

            killOrthogonalVelocity(physicsComponent.body);

        } else {
            physicsComponent.body.setAngularDamping(1f);
        }
    }

    private static Vec2 calculateLeftTurnPoint(Vec2 forwardVector, Vec2 center, float width){
        Vec2 vec = VectorUtils.rotate(forwardVector, 90);
        vec = vec.mul(width /2f);
        vec = vec.add(center);
        return vec;
    }

    private static Vec2 calculateRightTurnPoint(Vec2 forwardVector, Vec2 center, float width){
        return VectorUtils.rotate(forwardVector, -90).mul(width /2f).add(center);
    }
}
