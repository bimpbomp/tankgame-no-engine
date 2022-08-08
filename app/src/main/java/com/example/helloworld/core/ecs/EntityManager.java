package com.example.helloworld.core.ecs;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class EntityManager {
    private final Queue<Entity> availableEntities;

    public EntityManager() {
        availableEntities = new ArrayBlockingQueue<>(EcsConstants.MAX_ENTITIES);

        for (int i = 0; i < EcsConstants.MAX_ENTITIES; i++){
            availableEntities.add(new Entity(i));
        }
    }

    public Entity createEntity(){
        assert(availableEntities.size() > 0);
        return availableEntities.poll();
    }

    public void destroyEntity(Entity entity){
        //ensure entity is in range
        assert(entity.id < EcsConstants.MAX_ENTITIES);

        entity.signature.clear();
        availableEntities.add(entity);
    }
}
