package com.example.helloworld.core.android;

import android.view.MotionEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.Publisher;
import com.example.helloworld.core.observer.PublisherHub;
import com.example.helloworld.systems.input.SystemInputEvent;
import com.example.helloworld.systems.input.SystemInputType;
import org.jbox2d.common.Vec2;

public class AndroidInputSystem {
    private final Publisher publisher;

    public AndroidInputSystem(){
        this.publisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.SYSTEM_INPUT, publisher);
    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        boolean eventHandled = true;
        int eventIndex = motionEvent.getActionIndex();
        int pointerId = motionEvent.getPointerId(eventIndex);
        Vec2 eventPosition = new Vec2(motionEvent.getX(eventIndex), motionEvent.getY(eventIndex));
        SystemInputType systemInputType;

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                systemInputType = SystemInputType.POINTER_DOWN;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                systemInputType = SystemInputType.POINTER_UP;
                break;
            case MotionEvent.ACTION_MOVE:
                systemInputType = SystemInputType.POINTER_MOVE;
                break;
            default:
                return false;
        }

        publisher.notify(new SystemInputEvent(systemInputType, pointerId, eventPosition));

        return eventHandled;
    }
}