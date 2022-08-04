package com.example.helloworld.ecs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class System {
    private static final Map<String, Integer> systemTypes = new HashMap<>();
    private static int nextSystemCounter = 0;
    private Signature signature;
    protected final Set<Entity> entities;

    public System(){
        signature = new Signature();
        entities = new HashSet<>();
    }

    protected void setSignature(Signature signature){
        this.signature = signature;
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
    }

    public boolean caresAboutEntity(Entity entity){
        return entity.signature.matches(signature);
    }

    public void entitySignatureChanged(Entity entity){
        if (!caresAboutEntity(entity))
            removeEntity(entity);
    }

    public abstract void update();

    public int getType(){
        return systemTypes.putIfAbsent(this.getClass().getName(), nextSystemCounter++);
    }
}
