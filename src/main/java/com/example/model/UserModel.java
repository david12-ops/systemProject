package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.core.type.TypeReference;

import io.github.cdimascio.dotenv.Dotenv;
import utils.JsonStorage;
import utils.Enums.Operation;
import utils.AplicationService;

public class UserModel extends JsonStorage<User> {

    static Dotenv dotenv = Dotenv.load();
    private List<Map.Entry<String, String>> errorList = new ArrayList<>();
    AplicationService service = AplicationService.getInstance(errorList);
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
        }
    }

    public void updateUser(User user, User updatedUser) {
        validateData(Operation.UPDATE, updatedUser.getMailAccount(), updatedUser.getPassword());
        if (errorList.size() == 0) {
            updateItem(user, updatedUser);
        }
    }

    private boolean validateData(Operation operation, String email, String password) {
        return service.getValidationHandler().validateUserData(email, password)
                && service.getValidationHandler().nonDuplicateUserWithEmail(operation, email, this.listOfUsers);
    }

    public String getErrors() {
        return service.getErrHandler().getUserFriendlyMessage();
    }

    public User getUserByCredentials(String email, String password) {
        for (User user : this.listOfUsers) {
            if (user.getMailAccount().equals(email) && comparePassword(password, user.getPassword())) {
                return user;
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
