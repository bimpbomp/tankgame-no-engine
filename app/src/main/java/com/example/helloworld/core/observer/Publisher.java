package com.example.helloworld.core.observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Publisher {
    private final List<ISubscriber> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void addSubscriber(ISubscriber subscriber){
        subscribers.add(subscriber);
    }

    public void addSubscriber(ISubscriber[] subscribers){
        this.subscribers.addAll(Arrays.asList(subscribers));
    }

    public void removeSubscriber(ISubscriber subscriber){
        subscribers.remove(subscriber);
    }

    public void notify(GameEvent event){
        for (ISubscriber subscriber : subscribers){
            subscriber.onNotify(event);
        }
    }

    public ISubscriber[] getSubscribers(){
        return subscribers.toArray(new ISubscriber[0]);
    }
}
