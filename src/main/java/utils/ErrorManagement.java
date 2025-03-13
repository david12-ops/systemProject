package utils;

import java.util.HashMap;
import java.util.Map.Entry;

public interface ErrorManagement {

    void logError(Entry<String, String> error);

    boolean retryOperation(Runnable operation, int maxRetries);

    HashMap<String, String> getErrors();

    void clearErrorList();

}
