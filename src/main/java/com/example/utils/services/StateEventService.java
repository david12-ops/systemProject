package com.example.utils.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class StateEventService {
    private static final StateEventService instance = new StateEventService();
    private final HashMap<String, HashSet<Consumer<Object>>> listeners = new HashMap<>();

    private StateEventService() {
    }

    public static StateEventService getInstance() {
        return instance;
    }

    // Register a function to be called on an event
    public void subscribe(String eventName, Consumer<Object> callback) {
        HashSet<Consumer<Object>> eventListeners = listeners.get(eventName);
        if (eventListeners == null) {
            eventListeners = new HashSet<>();
            listeners.put(eventName, eventListeners);
        }

        eventListeners.add(callback);
    }

    // Fire an event and notify all subscribers
    public void emit(String eventName, Object playloud) {
        if (listeners.containsKey(eventName)) {
            for (Consumer<Object> listener : listeners.get(eventName)) {
                listener.accept(playloud);
            }
        }
    }

    // Stop listening to an event
    public void unsubscribe(String eventName, Consumer<Object> callback) {
        if (listeners.containsKey(eventName)) {
            listeners.get(eventName).remove(callback);
        }
    }
}
