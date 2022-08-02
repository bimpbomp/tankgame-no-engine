package com.example.helloworld.ecs;

public class Coordinator {
    private ComponentManager componentManager;
    private EntityManager entityManager;
    private SystemManager systemManager;

    public Coordinator() {
        this.componentManager = new ComponentManager();
        this.entityManager = new EntityManager();
        this.systemManager = new SystemManager();
    }

    public Entity createEntity(){
        return entityManager.createEntity();
    }

    public void destroyEntity(Entity entity){
        entityManager.destroyEntity(entity);
        componentManager.destroyEntity(entity);
        systemManager.destroyEntity(entity);
    }
}
