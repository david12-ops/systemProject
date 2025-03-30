package utils;

public class SessionContext {
    // secure place to hold sessionId
    private static final ThreadLocal<String> sessionHolder = new ThreadLocal<>();

    public static void setSessionId(String sessionId) {
        sessionHolder.set(sessionId);
    }

    public static String getSessionId() {
        return sessionHolder.get();
    }

    public static void clear() {
        sessionHolder.remove();
    }
}
