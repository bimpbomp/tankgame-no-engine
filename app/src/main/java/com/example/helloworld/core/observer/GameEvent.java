package com.example.helloworld.core.observer;

public abstract class GameEvent {
    private final GameEventType eventType;

    public GameEvent(GameEventType eventType) {
        this.eventType = eventType;
    }

    public GameEventType getEventType(){
        return this.eventType;
    }
}
