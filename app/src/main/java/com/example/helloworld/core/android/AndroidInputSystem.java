package com.example.helloworld.core.android;

import android.view.MotionEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.Publisher;
import com.example.helloworld.core.observer.PublisherHub;
import com.example.helloworld.systems.input.SystemInputEvent;
import com.example.helloworld.systems.input.SystemInputEventType;
import org.jbox2d.common.Vec2;

public class AndroidInputSystem {
    private final Publisher publisher;

    public AndroidInputSystem(){
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.SYSTEM_INPUT_EVENT, publisher);
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        boolean eventHandled = true;
        int eventIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(eventIndex);
        Vec2 eventPosition = new Vec2(motionEvent.getX(eventIndex), motionEvent.getY(eventIndex));
        SystemInputEventType systemInputEventType;

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                systemInputEventType = SystemInputEventType.POINTER_DOWN;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                systemInputEventType = SystemInputEventType.POINTER_UP;
                break;
            case MotionEvent.ACTION_MOVE:
                systemInputEventType = SystemInputEventType.POINTER_MOVE;
                break;
            default:
                return false;
        }

        publisher.notify(new SystemInputEvent(systemInputEventType, pointerId, eventPosition));

        return eventHandled;
    }
}