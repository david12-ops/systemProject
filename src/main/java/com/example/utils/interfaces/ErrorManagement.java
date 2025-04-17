package com.example.utils.interfaces;

import java.util.Map.Entry;

public interface ErrorManagement {

    void logError(Entry<String, String> error);

    boolean retryOperation(Runnable operation, int maxRetries);

    void removeError(String errorName);

    String getError(String errorName);
}
