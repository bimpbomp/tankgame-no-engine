package com.example.helloworld.core.ecs;

public class Coordinator {
    private ComponentManager componentManager;
    private EntityManager entityManager;
    private SystemManager systemManager;
    private Entity playerViewport;

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

    public Component getComponent(Entity entity, Class componentClass){
        int componentType = Component.getType(componentClass);
        return componentManager.getComponent(entity, componentType);
    }

    public void registerSystem(GameSystem system){
        systemManager.registerSystem(system);
    }

    public Entity getPlayerViewport() {
        return playerViewport;
    }

    public void setPlayerViewport(Entity playerViewport) {
        this.playerViewport = playerViewport;
    }
}
