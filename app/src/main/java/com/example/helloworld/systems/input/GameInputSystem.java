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
    private int count;

    public GameInputSystem(Coordinator coordinator) {
        super(coordinator);
        inputBuffer = new ConcurrentLinkedDeque<>();
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.UI_CONTEXT_CHANGE, publisher);
        PublisherHub.getInstance().subscribe(GameEventType.SYSTEM_INPUT_EVENT, this);
        count = 0;
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
            Vec2 eventLocation = event.coordinates;
            int eventId = event.pointerId;

            //todo process event. Consider how to prevent us being in an infinite loop here if system keeps adding more input
            // maybe need to have the onNotify method update some sort of mapping/context instead of adding to a buffer.
            Log.d("Input", "Input event type: " + event.systemInputEventType);

            for (Entity entity : entities){
                Ui uiComponent = (Ui) coordinator.getComponent(entity, Ui.class);

                switch (event.systemInputEventType){

                    case POINTER_UP:
                        if (uiComponent.pointerId == eventId)
                            uiComponent.onDeactivate.onDeactivate();
                        break;
                    case POINTER_DOWN:
                        if (containsCoordinates(entity, uiComponent, eventLocation) && uiComponent.pointerId < 0) {
                            uiComponent.onActivate.onActivate();
                            uiComponent.pointerId = eventId;
                        }
                        break;
                    case POINTER_MOVE:
                        break;
                }
            }

//            if (event.systemInputEventType == SystemInputEventType.POINTER_DOWN) {
//                if (count == 0){
//                    publisher.notify(new UiContextChangeEvent(UiContextState.NONE, UiContextState.LOADING));
//                } else {
//                    if (count % 2 == 0) {
//                        publisher.notify(new UiContextChangeEvent(UiContextState.LEVEL, UiContextState.LOADING));
//                    } else {
//                        publisher.notify(new UiContextChangeEvent(UiContextState.LOADING, UiContextState.LEVEL));
//                    }
//                }
//                count++;
//            }

            event = inputBuffer.peek();
        }
    }

    private boolean containsCoordinates (Entity uiEntity, Ui uiComponent, Vec2 coordinates){
        float left = uiEntity.position.x - uiComponent.width / 2f;
        float top = uiEntity.position.y - uiComponent.height / 2f;
        float right = uiEntity.position.x + uiComponent.width / 2f;
        float bottom = uiEntity.position.y + uiComponent.height / 2f;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(left);
        stringBuilder.append(", ");
        stringBuilder.append(top);
        stringBuilder.append("), (");
        stringBuilder.append(right);
        stringBuilder.append(", ");
        stringBuilder.append(bottom);
        stringBuilder.append(") ");

        Log.d("Input", stringBuilder.toString());
        Log.d("Input", coordinates.toString());

        return left < coordinates.x && top < coordinates.y && right > coordinates.x && bottom > coordinates.y;
    }

    @Override
    public void onNotify(GameEvent event) {
        inputBuffer.add((SystemInputEvent) event);
    }
}
