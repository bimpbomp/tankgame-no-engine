package com.example.helloworld.ecs;

import java.util.HashMap;
import java.util.Map;

public class ComponentArray {
    private Component[] componentArray;
    private Map<Entity, Integer> entityIntegerMap;
    private Map<Integer, Entity> integerEntityMap;
    private int topPointer;

    public ComponentArray(){
        componentArray = new Component[EcsConstants.MAX_ENTITIES];
        entityIntegerMap = new HashMap<>();
        integerEntityMap = new HashMap<>();
        topPointer = 0;
    }

    public void insertData(Entity entity, Component component){
        // Put new entry at end and update the maps
        int newIndex = topPointer;
        entityIntegerMap.put(entity, newIndex);
        integerEntityMap.put(newIndex, entity);
        componentArray[newIndex] = component;
        topPointer++;
    }

    public void removeData(Entity entity) {
        if (entityIntegerMap.containsKey(entity)){
            // Copy element at end into deleted element's place to maintain density
            int indexOfRemovedEntity = entityIntegerMap.get(entity);
            int indexOfLastElement = topPointer - 1;
            componentArray[indexOfRemovedEntity] = componentArray[indexOfLastElement];

            // Update map to point to moved spot
            Entity entityOfLastElement = integerEntityMap.get(indexOfLastElement);
            entityIntegerMap.put(entityOfLastElement, indexOfRemovedEntity);
            integerEntityMap.put(indexOfRemovedEntity, entityOfLastElement);

            entityIntegerMap.remove(entity);
            integerEntityMap.remove(indexOfLastElement);

            topPointer--;
        }
    }
}
