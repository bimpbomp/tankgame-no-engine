package com.example.helloworld.core.observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private final List<Subscriber> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void addObserver(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void removeObserver(Subscriber subscriber){
        subscribers.remove(subscriber);
    }

    public void notify(GameEvent event){
        for (Subscriber subscriber : subscribers){
            subscriber.onNotify(event);
        }
    }
}
