package com.example.helloworld.systems.viewport;

import com.example.helloworld.components.Viewport;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;

public class ViewportSystem extends GameSystem {
    public ViewportSystem(Coordinator coordinator) {
        super(coordinator);
        signature.set(Component.getType(Viewport.class));
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        Entity playerViewport = coordinator.getPlayerViewportEntity();
        playerViewport.position.set(coordinator.getPlayer().position);
    }
}
