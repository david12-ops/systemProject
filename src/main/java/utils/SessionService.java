package utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.model.User;

public class SessionService {

    // Session that manage multiple session of user, need implement something
    // specific to remove session when user logout
    private static SessionService instance;
    private Map<String, User> activeSessions;
    private Map<String, Instant> sessionExpirations;
    private Map<String, List<String>> userSessions;

    private SessionService() {
        this.activeSessions = new HashMap<>();
        this.sessionExpirations = new HashMap<>();
        this.userSessions = new HashMap<>();
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public String createSessionId(User user) {
        String sessionId = UUID.randomUUID().toString();
        Instant expirationTime = Instant.now().plusSeconds(3600);

        this.activeSessions.put(sessionId, user);
        this.sessionExpirations.put(sessionId, expirationTime);

        userSessions.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(sessionId);

        return sessionId;
    }

    private void removeExpiredSessions(String sessionId) {
        this.sessionExpirations.remove(sessionId);
        User user = this.activeSessions.remove(sessionId);

        if (user != null) {
            List<String> sessions = this.userSessions.get(user.getId());
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    this.userSessions.remove(user.getId());
                }
            }
        }
    }

    private boolean isSessionValid(String sessionId) {
        Instant expiration = this.sessionExpirations.get(sessionId);
        if (expiration == null || Instant.now().isAfter(expiration)) {
            removeExpiredSessions(sessionId);
            return false;
        }
        return true;
    }

    private List<String> getSessionIdsByUser(User user) {
        return this.userSessions.getOrDefault(user.getId(), new ArrayList<>());
    }

    public User getLoggedUser(User current) {
        List<String> ids = getSessionIdsByUser(current);

        for (String id : ids) {
            if (isSessionValid(id)) {
                return this.activeSessions.get(id);
            }
        }
        return null;
    }

    public boolean isUserLoggedIn(User current) {
        List<String> ids = getSessionIdsByUser(current);

        for (String id : ids) {
            if (isSessionValid(id)) {
                return true;
            }
        }
        return false;
    }

    public void removeSession(User current) {
        List<String> ids = getSessionIdsByUser(current);

        for (String id : ids) {
            removeExpiredSessions(id);
        }
    }
}
