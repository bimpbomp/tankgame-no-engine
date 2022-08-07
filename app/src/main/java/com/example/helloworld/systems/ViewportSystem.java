package com.example.helloworld.systems;

import android.util.Log;
import com.example.helloworld.components.Transform;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.ecs.Coordinator;
import com.example.helloworld.ecs.Entity;
import com.example.helloworld.ecs.GameSystem;

public class ViewportSystem extends GameSystem {
    public ViewportSystem(Coordinator coordinator) {
        super(coordinator);
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities){
            Viewport viewport = (Viewport) coordinator.getComponent(entity, Viewport.class);
            Entity focussedEntity = viewport.entityFocussedOn;

            Transform transform = (Transform) coordinator.getComponent(entity, Transform.class);
            Transform focussedEntityTransform = (Transform) coordinator.getComponent(focussedEntity, Transform.class);

            transform.position.set(focussedEntityTransform.position);
        }
    }
}
