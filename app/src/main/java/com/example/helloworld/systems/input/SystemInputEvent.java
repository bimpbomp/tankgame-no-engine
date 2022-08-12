package com.example.helloworld.systems.input;

import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;
import org.jbox2d.common.Vec2;

public class SystemInputEvent extends GameEvent {
    public final Vec2 coordinates;
    public final int pointerId;
    public final SystemInputType systemInputType;

    public SystemInputEvent(SystemInputType systemInputType, int pointerId, Vec2 coordinates) {
        super(GameEventType.SYSTEM_INPUT);
        this.systemInputType = systemInputType;
        this.pointerId = pointerId;
        this.coordinates = coordinates;
    }
}
