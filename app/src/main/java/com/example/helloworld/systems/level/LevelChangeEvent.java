package com.example.helloworld.systems.level;

import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;

public class LevelChangeEvent extends GameEvent {
    public LevelChangeEvent() {
        super(GameEventType.LEVEL_CHANGE);
    }
}
