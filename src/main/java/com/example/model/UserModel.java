package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import utils.JsonStorage;
import utils.AplicationService;

public class UserModel extends JsonStorage<User> {

    private static final String FILE_PATH = "/users.json";

    private List<Map.Entry<String, String>> errorList = new ArrayList<>();
    AplicationService service = AplicationService.getInstance(errorList);

    public UserModel() {
        super(FILE_PATH, new TypeReference<List<User>>() {
        });
        loadFromFile();
    }

    public void addUser(User user) {
        validateData(user.getMailAccount(), user.getPassword());
        if (service.getErrHandler().getSizeErrorList() == 0) {
            addItem(user);
            service.getErrHandler().clearErrorList();
        }
    }

    public void removeUser(User user) {
        removeItem(user);
    }

    public void updateUser(User user, User updatedUser) {
        validateData(updatedUser.getMailAccount(), updatedUser.getPassword());
        if (errorList.size() == 0) {
            updateItem(user, updatedUser);
            errorList.clear();
        }
    }

    protected void validateData(String email, String password) {
        service.getValidationHandler().validateUserData(email, password);
    }

    public User getUser(String senderUserEm) {
        for (User user : getItems()) {
            if (user.getMailAccount().equals(senderUserEm)) {
                return user;
            }

        }
        return null;
    }

    @Override
    protected List<User> createEmptyList() {
        List<User> listOfUsers = new ArrayList<>();
        return listOfUsers;
    }

}
