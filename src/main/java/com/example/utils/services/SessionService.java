package com.example.utils.services;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.example.model.User;
import com.example.model.UserToken;

public class SessionService {
    private static SessionService instance;
    // syncronized getInstance/hasMap for multi acces - prevent race conditions
    private ConcurrentHashMap<String, UserToken> activeSessions;

    private SessionService() {
        this.activeSessions = new ConcurrentHashMap<>();
    }

    public static SessionService getInstance() {
        if (instance == null) {
            synchronized (SessionService.class) {
                if (instance == null) {
                    instance = new SessionService();
                }
            }
        }

        return instance;
    }

    public String createSessionId(User user) {
        String sessionId = UUID.randomUUID().toString();
        this.activeSessions.put(sessionId, new UserToken(user.getMailAccount(), user.getId()));
        return sessionId;
    }

    public UserToken getUserBySessionId(String id) {
        return this.activeSessions.get(id);
    }

    public boolean isUserLoggedIn(String userId) {
        return this.activeSessions.values().stream().anyMatch(user -> user.getId().equals(userId));
    }

    public void clearAllSessions() {
        this.activeSessions.clear();
    }

    public void removeSession(String id) {
        this.activeSessions.remove(id);
    }
}
