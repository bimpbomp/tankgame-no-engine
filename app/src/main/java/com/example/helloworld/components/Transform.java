package com.example.helloworld.components;

import com.example.helloworld.ecs.Component;
import org.jbox2d.common.Vec2;

public class Transform extends Component {
    public Vec2 position;
    public float angle;
    public float scale;
}
