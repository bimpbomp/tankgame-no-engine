package com.example.helloworld.systems.input;

import android.util.Log;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.ui.UiContextChangeEvent;
import com.example.helloworld.systems.ui.UiContextState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameInputSystem extends GameSystem implements Subscriber {
    private final Queue<SystemInputEvent> inputBuffer;
    private final Publisher publisher;
    private boolean test = false;
    private boolean test1 = false;

    public GameInputSystem(Coordinator coordinator) {
        super(coordinator);
        inputBuffer = new ConcurrentLinkedDeque<>();
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.UI_CONTEXT_CHANGE, publisher);
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
            Log.d("Input", "Input event type: " + event.systemInputEventType);

            if (event.systemInputEventType == SystemInputEventType.POINTER_DOWN) {
                if (!test1){
                    publisher.notify(new UiContextChangeEvent(UiContextState.NONE, UiContextState.LOADING));
                    test1 = true;
                } else if (test) {
                    publisher.notify(new UiContextChangeEvent(UiContextState.LEVEL, UiContextState.LOADING));
                    test = false;
                } else {
                    publisher.notify(new UiContextChangeEvent(UiContextState.LOADING, UiContextState.LEVEL));
                    test = true;
                }

            }

            event = inputBuffer.peek();
        }
    }

    @Override
    public void onNotify(GameEvent event) {
        inputBuffer.add((SystemInputEvent) event);
    }
}
