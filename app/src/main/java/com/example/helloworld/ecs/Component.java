package com.example.helloworld.ecs;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {
    private static final Map<String, Integer> componentTypes = new HashMap<>();
    private static int nextTypeCounter = 0;

    public Component(){

    }

    public int getType(){
        return componentTypes.putIfAbsent(this.getClass().getName(), nextTypeCounter++);
    }
}
