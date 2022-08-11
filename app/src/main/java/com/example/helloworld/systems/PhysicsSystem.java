package com.example.helloworld.systems;

import com.example.helloworld.components.PhysicsBody;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class PhysicsSystem extends GameSystem {
    private World world;
    private final Vec2 gravity = new Vec2(0f, 0f);
    private final int velocityIterations = 6;
    private final int positionIterations = 2;

    public PhysicsSystem(Coordinator coordinator){
        super(coordinator);
        world = new World(gravity);
    }

    public PhysicsBody createPhysicsBody(Vec2 position, boolean dynamic, int width, int height){
        PhysicsBody physicsBody = new PhysicsBody();
        //body definition
        BodyDef bd = new BodyDef();
        bd.position.set(position.x, position.y);
        bd.angle = 0;

        if (dynamic)
            bd.type = BodyType.DYNAMIC;
        else
            bd.type = BodyType.STATIC;

        Body body =  world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/100f, height/100f);

        //define fixture of the body.
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;
        body.createFixture(fd);

        physicsBody.body = body;
        physicsBody.bodyDef = bd;
        physicsBody.fixtureDef = fd;
        physicsBody.shape = shape;

        return physicsBody;
    }


    public void update(float delta){
        world.step(delta, velocityIterations, positionIterations);

        for (Entity entity : entities){
            PhysicsBody physicsBody = (PhysicsBody) coordinator.getComponent(entity, PhysicsBody.class);

            entity.position = physicsBody.body.getPosition();
            entity.angle = physicsBody.body.getAngle();
        }
    }
}
