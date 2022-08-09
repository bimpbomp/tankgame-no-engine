package com.example.helloworld.systems.ui;

import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;

public class UiContextChangeEvent extends GameEvent {
    public final UiContextState from;
    public final UiContextState to;

    public UiContextChangeEvent(UiContextState from, UiContextState to) {
        super(GameEventType.UI_CONTEXT_CHANGE);
        this.from = from;
        this.to = to;
    }
}
