package com.example.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.utils.JsonStorage;
import com.example.utils.enums.AddTypeOperation;
import com.example.utils.enums.Operation;
import com.example.utils.interfaces.ErrorManagement;
import com.example.utils.services.AplicationService;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;

public class UserModel extends JsonStorage<User> {

    static Dotenv dotenv = Dotenv.load();
    private HashMap<String, String> errorMap = new HashMap<>();
    AplicationService service = AplicationService.getInstance(errorMap);
    private List<User> listOfUsers;

    public UserModel() {
        super(dotenv.get("FILE_PATH_USERS"), new TypeReference<List<User>>() {
        });
        this.listOfUsers = getItems();
    }

    public void addUser(String emailAccount, String password, String confirmationPassword, UserToken userToken,
            AddTypeOperation addTypeOperation) {
        boolean valid = validateData(Operation.CREATE, emailAccount, password, confirmationPassword);

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
            service.getErrHandler().logError(
                    new AbstractMap.SimpleEntry<>("removeAccount", "User is not logged or removing active account"));
        }
    }

    public boolean updateUser(UserToken userToken, String newPassword, String confirmationPassword) {

        if (service.getValidationHandler().confirmedPassword(newPassword, confirmationPassword)) {

            User foundUser = getUserByToken(userToken);

            if (foundUser != null) {
                if (BCrypt.checkpw(confirmationPassword, foundUser.getPassword())) {
                    service.getErrHandler().logError(new AbstractMap.SimpleEntry<>("confirmPassword",
                            "New password must be different from the old password"));
                    return false;
                } else {
                    User updatedUser = new User(foundUser.getUserId(), foundUser.getGroupId(),
                            foundUser.getMailAccount(), BCrypt.hashpw(confirmationPassword, BCrypt.gensalt()));

                    updateItem(foundUser, updatedUser);
                    return getUserByCredentials(updatedUser.getMailAccount(), confirmationPassword, null) != null ? true
                            : false;

                }
            }

            return false;
        }

        return false;
    }

    public boolean updateUser(User user, String newPassword, String confirmationPassword) {

        if (user != null) {
            if (service.getValidationHandler().confirmedPassword(newPassword, confirmationPassword)) {

                if (BCrypt.checkpw(confirmationPassword, user.getPassword())) {
                    service.getErrHandler().logError(new AbstractMap.SimpleEntry<>("confirmPassword",
                            "New password must be different from the old password"));
                    return false;
                } else {
                    User updatedUser = new User(user.getUserId(), user.getGroupId(), user.getMailAccount(),
                            BCrypt.hashpw(confirmationPassword, BCrypt.gensalt()));

                    updateItem(user, updatedUser);
                    return getUserByCredentials(updatedUser.getMailAccount(), confirmationPassword, null) != null ? true
                            : false;
                }
            }

            return false;
        }

        return false;
    }

    public void updateUser(UserToken userToken, String base64) {
        User user = getUserByToken(userToken);
        if (user != null) {
            user.setImage(base64);
            updateItem(user, user);
        }
    }

    private boolean validateData(Operation operation, String email, String password, String confirmationPassword) {
        return service.getValidationHandler().validateUserData(email, password)
                && service.getValidationHandler().confirmedPassword(password, confirmationPassword)
                && service.getValidationHandler().nonDuplicateUserWithEmail(operation, email, this.listOfUsers);

    }

    public ErrorManagement getErrorHandler() {
        return service.getErrHandler();
    }

    public User getUserByCredentials(String emailAccount, String password, UserToken userToken) {
        if (this.listOfUsers.size() > 0 && this.listOfUsers != null) {
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
        for (User user : this.listOfUsers) {
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

        for (User user : this.listOfUsers) {
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
