package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void addUser(User user) {
        validateData(Operation.CREATE, user.getMailAccount(), user.getPassword());
        if (service.getErrHandler().getSizeErrorList() == 0) {
            addItem(user);
            service.getErrHandler().clearErrorList();
        }
    }

    public void removeUser(User user) {
        removeItem(user);
    }

    public void updateUser(User user, User updatedUser) {
        validateData(Operation.UPDATE, updatedUser.getMailAccount(), updatedUser.getPassword());
        if (errorList.size() == 0) {
            updateItem(user, updatedUser);
            service.getErrHandler().clearErrorList();
        }
    }

    protected void validateData(Operation operation, String email, String password) {
        service.getValidationHandler().validateUserData(email, password);
        service.getValidationHandler().duplicateUserWithEmail(operation, email, this.listOfUsers);
    }

    @Override
    protected List<User> createEmptyList() {
        this.listOfUsers = new ArrayList<>();
        return listOfUsers;
    }

}
