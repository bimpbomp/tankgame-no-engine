package com.example.helloworld.components;

import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Entity;

public class Viewport extends Component {
    public Entity entityFocussedOn = null;
    public int width = 0;
    public int height = 0;
}
