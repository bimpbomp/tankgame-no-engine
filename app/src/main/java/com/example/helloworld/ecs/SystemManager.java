package com.example.helloworld.ecs;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    private final List<GameSystem> systems;

    public SystemManager(){
        systems = new ArrayList<>();
    }

    public void registerSystem(GameSystem system){
        systems.add(system);
    }

    public void entityDestroyed(Entity entity){
        for (GameSystem system : systems){
            system.removeEntity(entity);
        }
    }

    public void entitySignatureChanged(Entity entity){
        Log.d("LOADING", "entity " + entity.id + " sig: " + entity.signature.toString());
        for (GameSystem system : systems){

            system.entitySignatureChanged(entity);
        }
    }
}
