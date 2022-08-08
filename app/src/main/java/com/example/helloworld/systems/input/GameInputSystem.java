package com.example.helloworld.systems.input;

import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.Subscriber;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameInputSystem extends GameSystem implements Subscriber {
    private final Queue<SystemInputEvent> inputBuffer;

    public GameInputSystem(Coordinator coordinator) {
        super(coordinator);
        inputBuffer = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void update(float delta) {
        // process input buffer based off current input context
        // will need an InputComponent that each interactable UI element has, that contains:
        // - where on the screen the element is
        // - what function to call when it is interacted with (likely so it can broadcast it's own event? e.g. player_move_up_event)

        SystemInputEvent event = inputBuffer.peek();

        while (event != null){
            event = inputBuffer.poll();

            //todo process event. Consider how to prevent us being in an infinite loop here if system keeps adding more input
            // maybe need to have the onNotify method update some sort of mapping/context instead of adding to a buffer.

            event = inputBuffer.peek();
        }
    }

    @Override
    public void onNotify(GameEvent event) {
        inputBuffer.add((SystemInputEvent) event);
    }
}
