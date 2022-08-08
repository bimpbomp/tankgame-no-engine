package com.example.helloworld.components;

import com.example.helloworld.core.ecs.Component;
import org.jbox2d.common.Vec2;

public class Transform extends Component {
    public Vec2 position = new Vec2();
    public float angle = 0f;
    public float scale = 1f;
}
