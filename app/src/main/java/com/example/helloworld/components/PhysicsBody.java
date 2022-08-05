package com.example.helloworld.components;

import com.example.helloworld.ecs.Component;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsBody extends Component {
    public Shape shape;
    public FixtureDef fixtureDef;
    public BodyDef bodyDef;
    public Body body;
}
