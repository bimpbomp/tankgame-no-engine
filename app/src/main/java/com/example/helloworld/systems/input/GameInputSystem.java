package com.example.helloworld.systems.input;

import android.util.Log;
import com.example.helloworld.components.Ui;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.ui.UiContextChangeEvent;
import com.example.helloworld.systems.ui.UiContextState;
import org.jbox2d.common.Vec2;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameInputSystem extends GameSystem implements ISubscriber {
    private final Queue<SystemInputEvent> inputBuffer;
    private final Publisher publisher;

    public GameInputSystem(Coordinator coordinator) {
        super(coordinator);
        inputBuffer = new ConcurrentLinkedDeque<>();
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.UI_CONTEXT_CHANGE, publisher);
        PublisherHub.getInstance().subscribe(GameEventType.SYSTEM_INPUT_EVENT, this);
    }

    @Override
    public void update(float delta) {
        // process input buffer based off current input context
        // will need an InputComponent that each interactable UI element has, that contains:
        // - where on the screen the element is
        // - what function to call when it is interacted with (likely so it can broadcast it's own event? e.g. player_move_up_event)

        SystemInputEvent event = inputBuffer.peek();
        Log.d("Input", "Number of input elements: " + entities.size());

        while (event != null){
            event = inputBuffer.poll();
            Vec2 eventLocation = event.coordinates;
            int eventId = event.pointerId;

            //todo process event. Consider how to prevent us being in an infinite loop here if system keeps adding more input
            // maybe need to have the onNotify method update some sort of mapping/context instead of adding to a buffer.
            Log.d("Input", "GameInput event type: " + event.systemInputEventType);

            for (Entity entity : entities){
                Ui uiComponent = (Ui) coordinator.getComponent(entity, Ui.class);

                switch (event.systemInputEventType){

                    case POINTER_UP:
                        if (uiComponent.pointerId == eventId)
                            uiComponent.onDeactivate.onDeactivate();
                        break;
                    case POINTER_DOWN:
                        if (uiComponent.containsCoordinate.containsCoordinate(eventLocation) && uiComponent.pointerId < 0) {
                            uiComponent.onActivate.onActivate();
                            uiComponent.pointerId = eventId;
                        }
                        break;
                    case POINTER_MOVE:
                        break;
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
