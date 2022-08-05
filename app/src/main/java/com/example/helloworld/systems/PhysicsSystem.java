package com.example.helloworld.systems;

import android.util.Log;
import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.components.Transform;
import com.example.helloworld.ecs.Coordinator;
import com.example.helloworld.ecs.Entity;
import com.example.helloworld.ecs.GameSystem;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class PhysicsSystem extends GameSystem {
    private World world;
    private final Vec2 gravity = new Vec2(0f, 10f);
    private final float timeStep = 1.0f / 60.f;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;

    public PhysicsSystem(Coordinator coordinator){
        super(coordinator);
        world = new World(gravity);
    }

    public PhysicsBody createPhysicsBody(Transform transform){
        PhysicsBody physicsBody = new PhysicsBody();
        //body definition
        BodyDef bd = new BodyDef();
        bd.position.set(transform.position.x, transform.position.y);
        bd.type = BodyType.DYNAMIC;

        //define shape of the body.
        CircleShape cs = new CircleShape();
        cs.m_radius = transform.scale;

        //define fixture of the body.
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        //create the body and add fixture to it
        Body body =  world.createBody(bd);
        body.createFixture(fd);

        physicsBody.body = body;
        physicsBody.bodyDef = bd;
        physicsBody.fixtureDef = fd;
        physicsBody.shape = cs;

        return physicsBody;
    }


    public void update(float delta){
        world.step(timeStep, velocityIterations, positionIterations);
        Log.d("PHYSICS", "no. entities " + entities.size());

        for (Entity entity : entities){
            Transform transform = (Transform) coordinator.getComponent(entity, Transform.class);
            PhysicsBody physicsBody = (PhysicsBody) coordinator.getComponent(entity, PhysicsBody.class);

            transform.position = physicsBody.body.getPosition();
            transform.angle = physicsBody.body.getAngle();
        }
    }
}
