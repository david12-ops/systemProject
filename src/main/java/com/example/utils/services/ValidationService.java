package com.example.utils.services;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import com.example.model.User;
import com.example.utils.ErrorToolManager;
import com.example.utils.enums.Form;
import com.example.utils.enums.Operation;
import com.example.utils.interfaces.MessageModelValidationsTools;
import com.example.utils.interfaces.UserModelValidationsTools;

public class ValidationService {
    private static final Pattern EMAIL_REGEX = Pattern
            .compile("^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final long MAX_FILE_SIZE = 25L * 1024 * 1024;
    private static final Pattern PASSWORD_REGEX = Pattern
            .compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final String SUPPORTED_IMAGE_FILES = "(?i).*\\.(png|jpg|jpeg|gif)$";
    private static final String SUPPORTED_FILES = "(?i).*\\.(docx?|xlsx?|pptx?|pdf|txt|rtf|odt|ods|odp|jpg|jpeg|png|gif|bmp|tiff|webp|mp4|mov|avi|wmv|mp3|wav|m4a|zip|7z|tar|gz)$";

    public class UserModelValidations implements UserModelValidationsTools {
        private ErrorToolManager errorToolManager;

        public UserModelValidations(ErrorToolManager errorToolManager) {
            this.errorToolManager = errorToolManager;
        }

        @Override
        public boolean validEmail(String email) {

            boolean valid = true;

            if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
                errorToolManager.logError(errorToolManager.createErrorBody("email",
                        "Please enter a valid email address (e.g., user@example.com)."));
                valid = false;
            }

            return valid;
        }

        @Override
        public boolean validPassword(String currentPassword, String password, String email, Form form) {

            int atIndex = email.indexOf('@');
            String emailPart = atIndex == -1 ? null : email.substring(0, atIndex).toLowerCase().trim();
            String lowerPassword = password.toLowerCase();
            boolean valid = true;

            if (password == null || !PASSWORD_REGEX.matcher(password).matches()) {
                if (form == Form.ADDACCOUNT || form == Form.REGISTER) {
                    errorToolManager.logError(errorToolManager.createErrorBody("password",
                            "Password must include uppercase, lowercase, number, and special character, and be at least 8 characters"));
                    valid = false;
                }

                if (form == Form.FORGOTCREDENTIALS) {
                    errorToolManager.logError(errorToolManager.createErrorBody("newPassword",
                            "New password must include uppercase, lowercase, number, and special character, and be at least 8 characters"));
                    valid = false;
                }
            }

            if (emailPart != null) {

                if ((form == Form.ADDACCOUNT || form == Form.REGISTER) && lowerPassword.contains(emailPart)
                        && emailPart.length() >= 4) {
                    errorToolManager.logError(
                            errorToolManager.createErrorBody("password", "Password is too similar to your email"));
                    valid = false;
                }

                if (form == Form.FORGOTCREDENTIALS && lowerPassword.contains(emailPart) && emailPart.length() >= 4) {
                    errorToolManager.logError(errorToolManager.createErrorBody("newPassword",
                            "New password is too similar to your email"));
                    valid = false;
                }

            }

            if (currentPassword != null) {
                if ((form == Form.ADDACCOUNT || form == Form.REGISTER) && BCrypt.checkpw(password, currentPassword)) {
                    errorToolManager.logError(errorToolManager.createErrorBody("password",
                            "Password must be different from the current password"));
                    valid = false;
                }

                if (form == Form.FORGOTCREDENTIALS && BCrypt.checkpw(password, currentPassword)) {
                    errorToolManager.logError(errorToolManager.createErrorBody("newPassword",
                            "New password must be different from the current password"));
                    valid = false;
                }
            }

            return valid;
        }

        @Override
        public boolean nonDuplicateUserWithEmail(Operation operation, String senderEmail, List<User> users) {
            if (users != null && !users.isEmpty()) {
                List<User> list = operation == Operation.UPDATE ? users.stream()
                        .filter(user -> !user.getMailAccount().equals(senderEmail)).collect(Collectors.toList())
                        : users;

                for (User user : list) {
                    if (user.getMailAccount().equals(senderEmail)) {
                        errorToolManager
                                .logError(errorToolManager.createErrorBody("email", "Provided email is already used"));
                        return false;
                    }
                }

                return true;
            }
            return true;
        }

        @Override
        public boolean confirmedPassword(String password, String confirmationPassword, Form form) {

            boolean valid = true;

            if (!password.equals(confirmationPassword)) {
                if (form == Form.ADDACCOUNT || form == Form.REGISTER) {
                    errorToolManager.logError(errorToolManager.createErrorBody("confirmPassword",
                            "Password does not match the confirmation"));
                }

                if (form == Form.FORGOTCREDENTIALS) {
                    errorToolManager.logError(errorToolManager.createErrorBody("confirmNewPassword",
                            "New password does not match the confirmation"));
                }

                valid = false;
            }

            return valid;
        }

        @Override
        public boolean validProfileImage(File profileImage) {
            if (profileImage != null) {
                String name = profileImage.getName().toLowerCase();
                if (!name.matches(SUPPORTED_IMAGE_FILES)) {
                    errorToolManager.logError(
                            errorToolManager.createErrorBody("file", "Unsupported file type for profile image"));
                    return false;
                }

                return true;
            }

            return true;
        }
    }

    public class MessageModelValidations implements MessageModelValidationsTools {
        private ErrorToolManager errorToolManager;

        public MessageModelValidations(ErrorToolManager errorToolManager) {
            this.errorToolManager = errorToolManager;
        }

        @Override
        public boolean validFiles(List<File> files) {
            boolean valid = true;

            for (File file : files) {
                String fileName = file.getName().toLowerCase();

                if (file.length() > MAX_FILE_SIZE) {
                    errorToolManager.logError(errorToolManager.createErrorBody("file", "The file \"" + file.getName()
                            + "\" is too big — only files smaller than 25 MB can be sent."));
                    valid = false;
                }

                if (!fileName.matches(SUPPORTED_FILES)) {
                    errorToolManager.logError(
                            errorToolManager.createErrorBody("file", "Unsupported file type: " + file.getName()));
                    valid = false;
                }
            }

            return valid;
        }

        @Override
        public boolean validMessageData(String sender, String acceptor, String subject, String message,
                List<String> kopies) {

            boolean valid = true;

            if (sender == null || !EMAIL_REGEX.matcher(sender).matches()) {
                errorToolManager.logError(errorToolManager.createErrorBody("sender",
                        "Please enter a valid email address (e.g., user@example.com)."));
                valid = false;
            }

            if (acceptor == null || !EMAIL_REGEX.matcher(acceptor).matches()) {
                errorToolManager.logError(errorToolManager.createErrorBody("acceptor",
                        "Please enter a valid email address (e.g., user@example.com)."));
                valid = false;
            }

            if (subject != null && subject.length() > 500) {
                errorToolManager.logError(errorToolManager.createErrorBody("subject", "Subject is too long"));
                valid = false;
            }

            if (message != null && message.length() > 10000) {
                errorToolManager.logError(errorToolManager.createErrorBody("message", "Message is too long"));
                valid = false;
            }

            if (kopies != null && kopies.size() > 500) {

                errorToolManager.logError(errorToolManager.createErrorBody("kopies",
                        "Too many recipients — you can't send more than 500 at once."));
                valid = false;
            }

            return valid;
        }
    }
}
