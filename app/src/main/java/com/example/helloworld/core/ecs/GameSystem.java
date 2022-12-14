package com.example.helloworld.core.ecs;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class GameSystem {
    private static final Map<String, Integer> systemTypes = new HashMap<>();
    private static int nextSystemCounter = 0;
    protected final Signature signature;
    protected Set<Entity> entities;
    protected final Coordinator coordinator;

    public GameSystem(Coordinator coordinator){
        signature = new Signature();
        entities = new HashSet<>();
        this.coordinator = coordinator;
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public boolean caresAboutEntity(Entity entity){
        return entity.signature.contains(this.signature);
    }

    public void entitySignatureChanged(Entity entity){
        if (caresAboutEntity(entity))
            addEntity(entity);
        else
            removeEntity(entity);
    }

    public abstract void init();

    public abstract void update(float delta);

    public int getType(){
        if (systemTypes.containsKey(this.getClass().getName())){
            return systemTypes.get(this.getClass().getName());
        } else {
            int type = nextSystemCounter++;
            systemTypes.put(this.getClass().getName(), type);
            return type;
        }
    }

    public static int getType(Class clazz){
        if (systemTypes.containsKey(clazz.getName())){
            return systemTypes.get(clazz.getName());
        } else {
            int type = nextSystemCounter++;
            systemTypes.put(clazz.getName(), type);
            return type;
        }
    }
}
