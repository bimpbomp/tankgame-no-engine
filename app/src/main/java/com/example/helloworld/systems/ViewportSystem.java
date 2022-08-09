package com.example.helloworld.systems;

import com.example.helloworld.components.Viewport;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;

public class ViewportSystem extends GameSystem {
    public ViewportSystem(Coordinator coordinator) {
        super(coordinator);
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities){
            Viewport viewport = (Viewport) coordinator.getComponent(entity, Viewport.class);
            Entity focussedEntity = viewport.entityFocussedOn;

            entity.position.set(focussedEntity.position);
        }
    }
}