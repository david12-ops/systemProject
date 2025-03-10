package utils;

import java.util.Map.Entry;

public interface ErrorManagement {

    void logError(Entry<String, String> error);

    boolean retryOperation(Runnable operation, int maxRetries);

    String getUserFriendlyMessage();

    void clearErrorList();

}
