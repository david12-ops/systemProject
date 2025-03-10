package utils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.example.model.User;

import utils.Enums.Operation;

public class AplicationService {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_REGEX = Pattern
            .compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private ErrorManagerImpl errManager;
    private ValidationManagerImpl validationManager;
    private static AplicationService instance;

    private AplicationService(List<Map.Entry<String, String>> errorList) {
        this.errManager = new ErrorManagerImpl(errorList);
        this.validationManager = new ValidationManagerImpl(this.errManager);
    }

    public static AplicationService getInstance(List<Map.Entry<String, String>> errorList) {
        if (instance == null) {
            instance = new AplicationService(errorList);
        }
        return instance;
    }

    private static class ValidationManagerImpl implements ValidationManagement {

        private final ErrorManagement errManager;

        public ValidationManagerImpl(ErrorManagement errManager) {
            this.errManager = errManager;
        }

        @Override
        public boolean validateUserData(String email, String password) {
            int atIndex = email.indexOf('@');
            boolean valid = true;

            if (!EMAIL_REGEX.matcher(email).matches()) {
                this.errManager
                        .logError(new AbstractMap.SimpleEntry<>("email", "Provided email is not in correct format"));
                valid = false;
            }

            if (!PASSWORD_REGEX.matcher(password).matches()) {
                this.errManager.logError(
                        new AbstractMap.SimpleEntry<>("password", "Provided password is not in correct format"));
                valid = false;
            }

            if (password.toLowerCase().contains(email.substring(0, atIndex).toLowerCase().trim())) {
                this.errManager.logError(
                        new AbstractMap.SimpleEntry<>("password", "Password can not contains part of your email"));
                valid = false;
            }

            return valid;
        }

        @Override
        public boolean validateMessageData(String sender, String acceptor) {
            boolean valid = true;
            if (!EMAIL_REGEX.matcher(sender).matches()) {
                this.errManager
                        .logError(new AbstractMap.SimpleEntry<>("sender", "Provided sender is not in correct format"));
                valid = false;
            }

            if (!EMAIL_REGEX.matcher(acceptor).matches()) {
                this.errManager.logError(
                        new AbstractMap.SimpleEntry<>("acceptor", "Provided acceptor is not in correct format"));
                valid = false;
            }
            return valid;
        }

        @Override
        public boolean nonDuplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users) {
            if (users != null && !users.isEmpty()) {
                List<User> list = operation == Operation.UPDATE ? users.stream()
                        .filter(user -> !user.getMailAccount().equals(senderUserEm)).collect(Collectors.toList())
                        : users;

                for (User user : list) {
                    if (user.getMailAccount().equals(senderUserEm)) {
                        this.errManager.logError(
                                new AbstractMap.SimpleEntry<>("duplicateEmail", "Provided email is already used"));
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

    }

    private static class ErrorManagerImpl implements ErrorManagement {

        private final List<Map.Entry<String, String>> errorList;

        public ErrorManagerImpl(List<Map.Entry<String, String>> errorList) {
            this.errorList = errorList;
        }

        @Override
        public void logError(Entry<String, String> error) {
            this.errorList.add(error);
        }

        @Override
        public boolean retryOperation(Runnable operation, int maxRetries) {
            int attempts = 0;
            while (attempts < maxRetries) {
                try {
                    operation.run();
                    return true;
                } catch (Exception e) {
                    attempts++;
                }
            }
            return false;
        }

        @Override
        public String getUserFriendlyMessage() {
            return this.errorList.toString();
        }

        @Override
        public void clearErrorList() {
            this.errorList.clear();
        }

    }

    public ErrorManagement getErrHandler() {
        return this.errManager;
    }

    public ValidationManagement getValidationHandler() {
        return this.validationManager;
    }
}
