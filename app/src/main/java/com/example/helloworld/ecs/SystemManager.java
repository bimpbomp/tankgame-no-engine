package com.example.helloworld.ecs;

import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    private final List<System> systems;

    public SystemManager(){
        systems = new ArrayList<>();
    }

    public void registerSystem(System system){
        systems.add(system);
    }

    public void entityDestroyed(Entity entity){
        for (System system : systems){
            system.removeEntity(entity);
        }
    }

    public void entitySignatureChanged(Entity entity){
        for (System system : systems){
            system.entitySignatureChanged(entity);
        }
    }
}
