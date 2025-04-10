package com.example.utils;

public class SessionContext {
    // secure place to hold sessionId
    private static final ThreadLocal<String> sessionHolder = new ThreadLocal<>();
    // - JavaFX Does Not Guarantee the Same Thread for All Operations

    public static void setSessionId(String id) {
        sessionHolder.set(id);
    }

    public static String getSessionId() {
        return sessionHolder.get();
    }

    public static void clear() {
        sessionHolder.set(null);
    }
}
