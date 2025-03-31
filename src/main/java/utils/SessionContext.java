package utils;

public class SessionContext {
    // secure place to hold sessionId
    // private static final ThreadLocal<String> sessionHolder = new ThreadLocal<>();
    // - JavaFX Does Not Guarantee the Same Thread for All Operations

    private static String sessionId;

    public static void setSessionId(String id) {
        sessionId = id;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void clear() {
        sessionId = null;
    }
}
