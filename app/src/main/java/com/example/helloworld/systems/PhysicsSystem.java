package com.example.helloworld.systems;

import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
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

    public PhysicsBody createPhysicsBody(Entity entity){
        PhysicsBody physicsBody = new PhysicsBody();
        //body definition
        BodyDef bd = new BodyDef();
        bd.position.set(entity.position.x, entity.position.y);
        bd.type = BodyType.DYNAMIC;

        //define shape of the body.
        CircleShape cs = new CircleShape();
        cs.m_radius = entity.scale;

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

        for (Entity entity : entities){
            PhysicsBody physicsBody = (PhysicsBody) coordinator.getComponent(entity, PhysicsBody.class);

            entity.position = physicsBody.body.getPosition();
            entity.angle = physicsBody.body.getAngle();
        }
    }
}
