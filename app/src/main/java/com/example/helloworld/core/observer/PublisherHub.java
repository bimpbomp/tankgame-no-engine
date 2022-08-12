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

    public Publisher createNewPublisher(GameEventType gameEventType){
        Publisher publisher = new Publisher();
        addPublisher(gameEventType, publisher);
        return publisher;
    }

    public void addPublisher(GameEventType gameEventType, Publisher publisher){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).forEach(existingPublisher -> publisher.addSubscriber(existingPublisher.getSubscribers()));
            publishers.get(gameEventType).add(publisher);
        } else {
            List<Publisher> newPublisherType = new ArrayList<>();
            newPublisherType.add(publisher);
            publishers.put(gameEventType, newPublisherType);
        }
    }

    public void subscribe(GameEventType gameEventType, ISubscriber subscriber){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).forEach(publisher -> publisher.addSubscriber(subscriber));
        }
    }

    public void unsubscribe(GameEventType gameEventType, ISubscriber subscriber){
        if (publishers.containsKey(gameEventType)){
            publishers.get(gameEventType).forEach(publisher -> publisher.removeSubscriber(subscriber));
        }
    }

    public static PublisherHub getInstance(){
        if (instance == null){
            instance = new PublisherHub();
        }
        return instance;
    }
}
