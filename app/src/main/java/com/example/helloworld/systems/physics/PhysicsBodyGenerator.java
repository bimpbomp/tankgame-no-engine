package com.example.helloworld.systems.physics;

import com.example.helloworld.components.PhysicsComponent;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsBodyGenerator {
    public static PhysicsComponent createPhysicsBody(Vec2 position, boolean dynamic, float width, float height){
        PhysicsComponent physicsComponent = new PhysicsComponent();
        //body definition
        BodyDef bd = new BodyDef();
        bd.position.set(position.x, position.y);
        bd.angle = 0;

        if (dynamic)
            bd.type = BodyType.DYNAMIC;
        else
            bd.type = BodyType.STATIC;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        //define fixture of the body.
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.1f;

        physicsComponent.bodyDef = bd;
        physicsComponent.fixtureDef = fd;
        physicsComponent.shape = shape;

        return physicsComponent;
    }
}
