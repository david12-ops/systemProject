package com.example.utils.services;

import java.util.AbstractMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.example.model.User;
import com.example.utils.ErrorToolManager;
import com.example.utils.enums.Operation;
import com.example.utils.interfaces.ValidationManagement;

public class ValidationService implements ValidationManagement {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PASSWORD_REGEX = Pattern
            .compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private ErrorToolManager errorToolManager;

    public ValidationService(ErrorToolManager errorToolManager) {
        this.errorToolManager = errorToolManager;
    }

    @Override
    public boolean validateUserData(String email, String password) {
        int atIndex = email.indexOf('@');
        boolean valid = true;

        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            errorToolManager.logError(new AbstractMap.SimpleEntry<>("email",
                    "Please enter a valid email address (e.g., user@example.com)."));
            valid = false;
        }

        if (password == null || !PASSWORD_REGEX.matcher(password).matches()) {
            errorToolManager.logError(new AbstractMap.SimpleEntry<>("password",
                    "Password must include uppercase, lowercase, number, and special character, and be at least 8 characters."));
            valid = false;
        }

        if (atIndex != -1) {
            if (password.toLowerCase().contains(email.substring(0, atIndex).toLowerCase().trim())) {
                errorToolManager.logError(
                        new AbstractMap.SimpleEntry<>("password", "Password can not contains part of your email"));
                valid = false;
            }
        }

        return valid;
    }

    @Override
    public boolean validateMessageData(String sender, String acceptor) {
        boolean valid = true;
        if (sender == null || !EMAIL_REGEX.matcher(sender).matches()) {
            errorToolManager.logError(new AbstractMap.SimpleEntry<>("sender",
                    "Please enter a valid email address (e.g., user@example.com)."));
            valid = false;
        }

        if (acceptor == null || !EMAIL_REGEX.matcher(acceptor).matches()) {
            errorToolManager.logError(new AbstractMap.SimpleEntry<>("acceptor",
                    "Please enter a valid email address (e.g., user@example.com)."));
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean nonDuplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users) {
        if (users != null && !users.isEmpty()) {
            List<User> list = operation == Operation.UPDATE ? users.stream()
                    .filter(user -> !user.getMailAccount().equals(senderUserEm)).collect(Collectors.toList()) : users;

            for (User user : list) {
                if (user.getMailAccount().equals(senderUserEm)) {
                    errorToolManager.logError(new AbstractMap.SimpleEntry<>("email", "Provided email is already used"));
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

        if (newPassword == null || !PASSWORD_REGEX.matcher(newPassword).matches()) {
            errorToolManager.logError(new AbstractMap.SimpleEntry<>("newPassword",
                    "Password must include uppercase, lowercase, number, and special character, and be at least 8 characters."));
            valid = false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            errorToolManager.logError(
                    new AbstractMap.SimpleEntry<>("confirmPassword", "Provided new password do not match confirmed"));
            valid = false;
        }

        return valid;
    }
}
