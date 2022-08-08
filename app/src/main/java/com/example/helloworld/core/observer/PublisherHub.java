package com.example.helloworld.core.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublisherHub {
    private static PublisherHub instance;
    private final Map<GameEventType, List<Publisher>> publishers;

    public PublisherHub(){
        publishers = new HashMap<>();
    }

    public void addPublisher(GameEventType gameEventType, Publisher publisher){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).add(publisher);
        } else {
            List<Publisher> newPublisherType = new ArrayList<>();
            newPublisherType.add(publisher);
            publishers.put(gameEventType, newPublisherType);
        }
    }

    public void subscribe(GameEventType gameEventType, Subscriber subscriber){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).forEach(publisher -> publisher.addObserver(subscriber));
        }
    }

    public void unsubscribe(GameEventType gameEventType, Subscriber subscriber){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).forEach(publisher -> publisher.removeObserver(subscriber));
        }
    }

    public static PublisherHub getInstance(){
        if (instance == null){
            instance = new PublisherHub();
        }
        return instance;
    }
}
