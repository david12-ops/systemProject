package com.example.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.utils.ErrorToolManager;
import com.example.utils.ImageConvertor;
import com.example.utils.JsonStorage;
import com.example.utils.enums.AddTypeOperation;
import com.example.utils.enums.Form;
import com.example.utils.enums.Operation;
import com.example.utils.services.ValidationService;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;

public class UserModel extends JsonStorage<User> {

    static Dotenv dotenv = Dotenv.load();
    private HashMap<String, String> errorMap = new HashMap<>();
    private final ErrorToolManager errorToolManager = new ErrorToolManager(errorMap);
    private final ValidationService validationService = new ValidationService();
    private final ValidationService.UserModelValidations validator = validationService.new UserModelValidations(
            errorToolManager);
    private List<User> listOfUsers;

    public UserModel() {
        super(dotenv.get("FILE_PATH_USERS"), new TypeReference<List<User>>() {
        });
        this.listOfUsers = getItems();
    }

    public void addUser(String emailAccount, String password, String confirmationPassword, UserToken userToken,
            AddTypeOperation addTypeOperation, Form form) {
        boolean valid = validateData(Operation.CREATE, emailAccount, password, confirmationPassword, form);

        if (addTypeOperation == AddTypeOperation.NEWACCOUNT) {
            if (valid) {
                User newUser = new User(null, null, emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
                addItem(newUser);
            }
        }

        if (addTypeOperation == AddTypeOperation.ANOTHERACCOUNT) {
            User loggedUser = getUserByToken(userToken);
            if (valid && loggedUser != null) {
                User newUser = new User(null, loggedUser.getGroupId(), emailAccount,
                        BCrypt.hashpw(password, BCrypt.gensalt()));
                addItem(newUser);
            }
        }
    }

    public void removeUser(UserToken userToken, User user) {
        User loggedUser = getUserByToken(userToken);
        if (loggedUser != null && !loggedUser.getMailAccount().equals(user.getMailAccount())) {
            removeItem(user);
        } else {
            errorToolManager.logError(errorToolManager.createErrorBody("removedAccount",
                    "User is not logged or removing active account"));
        }
    }

    public void updateUser(UserToken userToken, String password, String confirmationPassword, Form form) {
        User foundUser = getUserByToken(userToken);
        if (foundUser != null) {
            boolean validPasswords = validator.validPassword(password, foundUser.getMailAccount(), form)
                    && validator.confirmedPassword(foundUser.getPassword(), password, confirmationPassword, form);

            if (validPasswords) {
                User updatedUser = new User(foundUser.getUserId(), foundUser.getGroupId(), foundUser.getMailAccount(),
                        BCrypt.hashpw(confirmationPassword, BCrypt.gensalt()));

                updateItem(foundUser, updatedUser);
            }
        }
    }

    public void updateUser(User user, String password, String confirmationPassword, Form form) {
        if (user != null) {
            boolean validPasswords = validator.validPassword(password, user.getMailAccount(), form)
                    && validator.confirmedPassword(user.getPassword(), password, confirmationPassword, form);

            if (validPasswords) {
                User updatedUser = new User(user.getUserId(), user.getGroupId(), user.getMailAccount(),
                        BCrypt.hashpw(confirmationPassword, BCrypt.gensalt()));

                updateItem(user, updatedUser);
            }
        }
    }

    public void updateUser(UserToken userToken, File profileImage) {
        User user = getUserByToken(userToken);
        if (user != null && validator.validProfileImage(profileImage)) {
            try {
                String base64 = profileImage != null ? ImageConvertor.imageToBase64(profileImage) : null;
                user.setImage(base64);
                updateItem(user, user);
            } catch (IOException e) {
                System.err.println("Error converting image: " + e.getMessage());
            }
        }
    }

    private boolean validateData(Operation operation, String email, String password, String confirmationPassword,
            Form form) {
        return validator.validEmail(email) && validator.nonDuplicateUserWithEmail(operation, email, listOfUsers)
                && validator.validPassword(password, email, form)
                && validator.confirmedPassword(null, password, confirmationPassword, form);
    }

    public String getError(String errorName) {
        return errorToolManager.getError(errorName);
    }

    public void clearError(String errorName) {
        errorToolManager.removeError(errorName);
    }

    public User getUserByCredentials(String emailAccount, String password, UserToken userToken) {
        if (listOfUsers != null && listOfUsers.size() > 0) {
            if (emailAccount != null && password != null && userToken == null) {
                return getUserByEmailAndPassword(emailAccount, password);
            }

            if (emailAccount == null && password == null && userToken != null) {
                return getUserByToken(userToken);
            }

            return null;
        } else {
            return null;
        }
    }

    private User getUserByEmailAndPassword(String email, String password) {
        for (User user : listOfUsers) {
            if (user.getMailAccount().equals(email) && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    private User getUserByToken(UserToken userToken) {
        if (userToken == null) {
            return null;
        }

        for (User user : listOfUsers) {
            if (user.getUserId().equals(userToken.getUserId())
                    && user.getMailAccount().equals(userToken.getMailAccount())) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUserAccounts(UserToken userToken) {
        if (getUserByToken(userToken) == null) {
            return null;
        }

        return listOfUsers.stream().filter(user -> user.getGroupId().equals(userToken.getGroupId())).toList();
    }

    @Override
    protected List<User> createEmptyList() {
        listOfUsers = new ArrayList<>();
        return listOfUsers;
    }

}
