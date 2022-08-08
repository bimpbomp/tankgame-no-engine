package com.example.helloworld.systems;

import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.GameSystem;

public class PlayerMovementSystem extends GameSystem {
    public PlayerMovementSystem(Coordinator coordinator) {
        super(coordinator);
    }

    @Override
    public void update(float delta) {
        /*
        * Handles inputs like PointerDown, PointerUp, PointerMove
        * */
    }
}
