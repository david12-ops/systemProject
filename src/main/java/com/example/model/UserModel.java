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

    // TODO - perzistent token for another features like logout etc.

    static Dotenv dotenv = Dotenv.load();
    private List<Map.Entry<String, String>> errorList = new ArrayList<>();
    AplicationService service = AplicationService.getInstance(errorList);
    private List<User> listOfUsers;
    private User currentUser;

    public UserModel() {
        super(dotenv.get("FILE_PATH_USERS"), new TypeReference<List<User>>() {
        });
        this.listOfUsers = getItems();
    }

    public void addUser(String emailAccount, String password) {
        boolean valid = validateData(Operation.CREATE, emailAccount, password);
        if (valid) {
            User newUser = new User(emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
            this.currentUser = newUser;
            addItem(newUser);
            // chytrejsi erroring
            // service.getErrHandler().clearErrorList();
        }
    }

    public void removeUser() {
        User userToRemove = new User("", this.currentUser.getMailAccount(), "");
        removeItem(userToRemove);
    }

    public void updateUser(User user, User updatedUser) {
        validateData(Operation.UPDATE, updatedUser.getMailAccount(), updatedUser.getPassword());
        if (errorList.size() == 0) {
            updateItem(user, updatedUser);
            service.getErrHandler().clearErrorList();
        }
    }

    protected boolean validateData(Operation operation, String email, String password) {
        return service.getValidationHandler().validateUserData(email, password)
                && service.getValidationHandler().nonDuplicateUserWithEmail(operation, email, this.listOfUsers);
    }

    public String getErrors() {
        return service.getErrHandler().getUserFriendlyMessage();
    }

    @Override
    protected List<User> createEmptyList() {
        this.listOfUsers = new ArrayList<>();
        return listOfUsers;
    }

}
