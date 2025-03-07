package utils;

import java.util.Map.Entry;

public interface ErrorManager {

    void logError(Entry<String, String> error);

    boolean retryOperation(Runnable operation, int maxRetries);

    String getUserFriendlyMessage(Exception e);

    void clearErrorList();

    Integer getSizeErrorList();
}
