package com.example.helloworld.systems.input;

import com.example.helloworld.components.Ui;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import org.jbox2d.common.Vec2;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameInputSystem extends GameSystem implements ISubscriber {
    private final Queue<SystemInputEvent> inputBuffer;
    private final Publisher publisher;

    public GameInputSystem(Coordinator coordinator) {
        super(coordinator);
        signature.set(Component.getType(Ui.class));

        inputBuffer = new ConcurrentLinkedDeque<>();
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.UI_CONTEXT_CHANGE, publisher);
        PublisherHub.getInstance().subscribe(GameEventType.SYSTEM_INPUT, this);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {

        SystemInputEvent event = inputBuffer.peek();

        while (event != null){
            event = inputBuffer.poll();
            Vec2 eventLocation = event.coordinates;
            int eventId = event.pointerId;

            //todo process event. Consider how to prevent us being in an infinite loop here if system keeps adding more input
            // maybe need to have the onNotify method update some sort of mapping/context instead of adding to a buffer.
            for (Entity entity : entities){
                Ui uiComponent = (Ui) coordinator.getComponent(entity, Ui.class);

                switch (event.systemInputType){

                    case POINTER_UP:
                        if (uiComponent.pointerId == eventId) {
                            uiComponent.onDeactivate.onDeactivate();
                            uiComponent.pointerId = -1;
                        }
                        break;
                    case POINTER_DOWN:
                        if (uiComponent.containsCoordinate.containsCoordinate(eventLocation) && uiComponent.pointerId < 0) {
                            uiComponent.onActivate.onActivate();
                            uiComponent.pointerId = eventId;
                        }
                        break;
                    case POINTER_MOVE:
                        if (!uiComponent.containsCoordinate.containsCoordinate(eventLocation) && uiComponent.pointerId == eventId) {
                            uiComponent.onDeactivate.onDeactivate();
                            uiComponent.pointerId = -1;
                        }
                        else if (uiComponent.containsCoordinate.containsCoordinate(eventLocation) && uiComponent.pointerId == eventId) {
                            uiComponent.onDrag.onDrag();
                        }
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
