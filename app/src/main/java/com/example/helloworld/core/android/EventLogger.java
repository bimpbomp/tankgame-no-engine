package com.example.helloworld.core.android;

import android.util.Log;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.PublisherHub;
import com.example.helloworld.core.observer.ISubscriber;
import com.example.helloworld.systems.input.SystemInputEvent;
import com.example.helloworld.systems.input.SystemInputEventType;

public class EventLogger implements ISubscriber {
    public EventLogger(){
        PublisherHub.getInstance().subscribe(GameEventType.SYSTEM_INPUT_EVENT, this);
    }

    @Override
    public void onNotify(GameEvent event) {
        if (event.getEventType() == GameEventType.SYSTEM_INPUT_EVENT){
            SystemInputEvent systemInputEvent = (SystemInputEvent) event;
            if (systemInputEvent.systemInputEventType == SystemInputEventType.POINTER_MOVE)
                return;

            Log.d("Logging", "Input Event: " + systemInputEvent.systemInputEventType + ", Pointer ID: " + systemInputEvent.pointerId + ", Coordinates: " + systemInputEvent.coordinates);
        }
    }
}
