package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.model.User;

public class SessionService {
    private static SessionService instance;
    private Map<String, User> activeSessions;

    private SessionService() {
        this.activeSessions = new HashMap<>();
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }

        return instance;
    }

    public String createSessionId(User user) {
        String sessionId = UUID.randomUUID().toString();
        this.activeSessions.put(sessionId, user);
        return sessionId;
    }

    public User getUserBySessionId(String id) {
        return this.activeSessions.get(id);
    }

    public boolean IsUserLoggedIn(String id) {
        return this.activeSessions.containsKey(id);
    }

    public void clearAllSessions() {
        this.activeSessions.clear();
    }

    public void removeSession(String id) {
        this.activeSessions.remove(id);
    }
}
