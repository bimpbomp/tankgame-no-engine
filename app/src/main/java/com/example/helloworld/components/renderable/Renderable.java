package com.example.helloworld.components.renderable;

import com.example.helloworld.core.ecs.Component;

public abstract class Renderable extends Component {
    public int zOrder;
    // maybe hacky, but allows UI elements to be defined relative to the viewport
    // defaulted to false so most game objects won't even care that this is here.
    public boolean isScreenElement = false;
}
