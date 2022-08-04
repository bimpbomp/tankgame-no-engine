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
        componentManager.entityDestroyed(entity);
        systemManager.entityDestroyed(entity);
    }

    public void registerComponentType(int componentType){
        componentManager.registerComponentType(componentType);
    }

    public void addComponent(Entity entity, Component component){
        componentManager.addComponent(entity, component);
        entity.signature.set(component.getType());
        systemManager.entitySignatureChanged(entity);
    }

    public void removeComponent(Entity entity, int componentType){
        componentManager.removeComponent(entity, componentType);
        entity.signature.clear(componentType);
    }

    public Component getComponent(Entity entity, int componentType){
        return componentManager.getComponent(entity, componentType);
    }

    public void registerSystem(System system){

    }
}
