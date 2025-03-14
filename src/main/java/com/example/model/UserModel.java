package com.example.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;
import utils.JsonStorage;
import utils.Enums.Operation;
import utils.AplicationService;

public class UserModel extends JsonStorage<User> {

    // TODO - test registration

    static Dotenv dotenv = Dotenv.load();
    private HashMap<String, String> errorMap = new HashMap<>();
    AplicationService service = AplicationService.getInstance(errorMap);
    private List<User> listOfUsers;

    public UserModel() {
        super(dotenv.get("FILE_PATH_USERS"), new TypeReference<List<User>>() {
        });
        this.listOfUsers = getItems();
    }

    public void addUser(String emailAccount, String password) {
        boolean valid = validateData(Operation.CREATE, emailAccount, password);
        if (valid) {
            User newUser = new User(null, emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
            addItem(newUser);
        }
    }

    public void removeUser(User activeUser, User user) {
        if (activeUser != null && !activeUser.getMailAccount().equals(user.getMailAccount())) {
            removeItem(user);
        } else {
            service.getErrHandler().logError(
                    new AbstractMap.SimpleEntry<>("removeAccount", "User is not logged or removing active account"));
        }
    }

    public User updateUser(User user, String newPassword, String confirmationPassword) {

        User updatedUser = null;

        if (user != null) {
            if (service.getValidationHandler().confirmedPassword(newPassword, confirmationPassword)) {

                User newUser = new User(user.getId(), user.getMailAccount(),
                        BCrypt.hashpw(confirmationPassword, BCrypt.gensalt()));
                updateItem(user, newUser);

                updatedUser = getUserByCredentials(newUser.getMailAccount(), newUser.getPassword());
            }
        }

        return updatedUser;
    }

    private boolean validateData(Operation operation, String email, String password) {
        return service.getValidationHandler().validateUserData(email, password)
                && service.getValidationHandler().nonDuplicateUserWithEmail(operation, email, this.listOfUsers);
    }

    public HashMap<String, String> getErrors() {
        return service.getErrHandler().getErrors();
    }

    public User getUserByCredentials(String email, String password) {

        if (this.listOfUsers.size() > 0 && this.listOfUsers != null) {
            for (User user : this.listOfUsers) {
                if (user.getMailAccount().equals(email) && comparePassword(password, user.getPassword())) {
                    return user;
                }
            }
        }
        return null;
    }

    private boolean comparePassword(String enteredPassword, String storedHash) {
        return BCrypt.checkpw(enteredPassword, storedHash);
    }

    @Override
    protected List<User> createEmptyList() {
        this.listOfUsers = new ArrayList<>();
        return listOfUsers;
    }

}
