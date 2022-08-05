package com.example.helloworld.ecs;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {
    private static final Map<String, Integer> componentTypes = new HashMap<>();
    private static int nextTypeCounter = 0;

    public Component(){

    }

    public int getType(){
        if (componentTypes.containsKey(this.getClass().getName())){
            return componentTypes.get(this.getClass().getName());
        } else {
            int type = nextTypeCounter++;
            componentTypes.put(this.getClass().getName(), type);
            return type;
        }
    }

    public static int getType(Class clazz){
        if (componentTypes.containsKey(clazz.getName())){
            return componentTypes.get(clazz.getName());
        } else {
            int type = nextTypeCounter++;
            componentTypes.put(clazz.getName(), type);
            return type;
        }
    }
}
