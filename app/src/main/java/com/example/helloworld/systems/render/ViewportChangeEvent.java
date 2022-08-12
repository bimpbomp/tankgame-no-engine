package com.example.helloworld.systems.render;

import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;

public class ViewportChangeEvent extends GameEvent {
    public final Entity viewport;
    public ViewportChangeEvent(Entity viewport) {
        super(GameEventType.VIEWPORT_CHANGE);
        this.viewport = viewport;
    }
}
