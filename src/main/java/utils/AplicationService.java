package utils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
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

    private AplicationService(HashMap<String, String> errorMap) {
        this.errManager = new ErrorManagerImpl(errorMap);
        this.validationManager = new ValidationManagerImpl(this.errManager);
    }

    public static AplicationService getInstance(HashMap<String, String> errorMap) {
        if (instance == null) {
            instance = new AplicationService(errorMap);
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

            if (atIndex != -1) {
                if (password.toLowerCase().contains(email.substring(0, atIndex).toLowerCase().trim())) {
                    this.errManager.logError(
                            new AbstractMap.SimpleEntry<>("password", "Password can not contains part of your email"));
                    valid = false;
                }
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
                        this.errManager
                                .logError(new AbstractMap.SimpleEntry<>("email", "Provided email is already used"));
                        return false;
                    }
                }
                return true;
            }
            return true;
        }

        @Override
        public boolean confirmedPassword(String newPassword, String confirmNewPassword) {

            boolean valid = true;

            if (!PASSWORD_REGEX.matcher(newPassword).matches()) {
                this.errManager.logError(
                        new AbstractMap.SimpleEntry<>("newPassword", "Provided password is not in correct format"));
                valid = false;
            }

            if (!PASSWORD_REGEX.matcher(confirmNewPassword).matches()) {
                this.errManager.logError(
                        new AbstractMap.SimpleEntry<>("confirmPassword", "Provided password is not in correct format"));
                valid = false;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                this.errManager.logError(new AbstractMap.SimpleEntry<>("confirmPassword",
                        "Provided new password do not match confirmed"));
                valid = false;
            }

            return valid;
        }

    }

    private static class ErrorManagerImpl implements ErrorManagement {

        private final HashMap<String, String> errorMap;

        public ErrorManagerImpl(HashMap<String, String> errorMap) {
            this.errorMap = errorMap;
        }

        @Override
        public void logError(Entry<String, String> error) {
            this.errorMap.put(error.getKey(), error.getValue());
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
        public HashMap<String, String> getErrors() {
            return this.errorMap;
        }

        @Override
        public void clearErrorList() {
            this.errorMap.clear();
        }

    }

    public ErrorManagement getErrHandler() {
        return this.errManager;
    }

    public ValidationManagement getValidationHandler() {
        return this.validationManager;
    }
}
