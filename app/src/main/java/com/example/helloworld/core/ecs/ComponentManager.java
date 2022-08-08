package com.example.helloworld.core.ecs;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    private Map<Integer, ComponentArray> componentArrays;

    public ComponentManager(){
        componentArrays = new HashMap<>();
    }

    public void registerComponentType(int componentType){
        componentArrays.put(componentType, new ComponentArray());
    }

    public void addComponent(Entity entity, Component component){
        componentArrays.get(component.getType()).insertData(entity, component);
    }

    public void removeComponent(Entity entity, int componentType){
        componentArrays.get(componentType).removeData(entity);
    }

    public Component getComponent(Entity entity, int componentType){
        return componentArrays.get(componentType).getData(entity);
    }

    public void entityDestroyed(Entity entity){
        for (Map.Entry<Integer, ComponentArray> entry : componentArrays.entrySet()){
            entry.getValue().removeData(entity);
        }
    }
}
