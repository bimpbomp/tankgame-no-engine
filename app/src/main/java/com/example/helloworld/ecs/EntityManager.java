package com.example.helloworld.ecs;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class EntityManager {
    private final Queue<Entity> availableEntities;
    private final Signature[] signatures;

    public EntityManager() {
        availableEntities = new ArrayBlockingQueue<>(EcsConstants.MAX_ENTITIES);
        signatures = new Signature[EcsConstants.MAX_ENTITIES];

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

        signatures[entity.id].reset();
        availableEntities.add(entity);
    }

    public void setSignature(Entity entity, Signature signature){
        //ensure entity is in range
        assert(entity.id < EcsConstants.MAX_ENTITIES);

        signatures[entity.id] = signature;
    }

    public Signature getSignature(Entity entity){
        //ensure entity is in range
        assert(entity.id < EcsConstants.MAX_ENTITIES);

        return signatures[entity.id];
    }
}
