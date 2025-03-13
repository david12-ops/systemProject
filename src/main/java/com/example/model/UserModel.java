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

    // TODO - registration (better comunication with model)

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
            User newUser = new User(emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
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

    public void updateUser(User user, User updatedUser) {
        boolean valid = validateData(Operation.UPDATE, updatedUser.getMailAccount(), updatedUser.getPassword());
        if (valid) {
            updateItem(user, updatedUser);
        }
    }

    private boolean validateData(Operation operation, String email, String password) {
        return service.getValidationHandler().validateUserData(email, password)
                && service.getValidationHandler().nonDuplicateUserWithEmail(operation, email, this.listOfUsers);
    }

    public HashMap<String, String> getErrors() {
        return service.getErrHandler().getErrors();
    }

    public User getUserByCredentials(String email, String password) {
        for (User user : this.listOfUsers) {
            if (user.getMailAccount().equals(email) && comparePassword(password, user.getPassword())) {
                return user;
            }
        }
        service.getErrHandler()
                .logError(new AbstractMap.SimpleEntry<>("searchUser", "User not found, invalid email or password"));
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
