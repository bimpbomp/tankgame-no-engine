package com.example.helloworld.core.android;

import android.util.Log;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.PublisherHub;
import com.example.helloworld.core.observer.ISubscriber;
import com.example.helloworld.systems.input.SystemInputEvent;
import com.example.helloworld.systems.input.SystemInputType;

public class EventLogger implements ISubscriber {
    public EventLogger(){
        PublisherHub.getInstance().subscribe(GameEventType.SYSTEM_INPUT, this);
    }

    @Override
    public void onNotify(GameEvent event) {
        if (event.getEventType() == GameEventType.SYSTEM_INPUT){
            SystemInputEvent systemInputEvent = (SystemInputEvent) event;
            if (systemInputEvent.systemInputType == SystemInputType.POINTER_MOVE)
                return;

            Log.d("Logging", "Input Event: " + systemInputEvent.systemInputType + ", Pointer ID: " + systemInputEvent.pointerId + ", Coordinates: " + systemInputEvent.coordinates);
        }
    }
}
