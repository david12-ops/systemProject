package com.example.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;

import utilities.JsonStorage;

public class UserModel extends JsonStorage<User> {

    private static final String FILE_PATH = "/users.json";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    // Error Mnager ?
    private List<Map.Entry<String, String>> errorList = new ArrayList<>();

    public UserModel() {
        super(FILE_PATH, new TypeReference<List<User>>() {
        });
        loadFromFile();
    }

    public void addUser(User user) {
        validateData(user.getMailAccount(), user.getPassword());
        if (errorList.size() == 0) {
            addItem(user);
            errorList.clear();
        }
    }

    // remove, update

    protected void validateData(String email, String password) {
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            errorList.add(new AbstractMap.SimpleEntry<>("email", "Provided email is not in correct format"));
        }

        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            errorList.add(new AbstractMap.SimpleEntry<>("password", "Provided password is not in correct format"));
        }
    }

    public List<Map.Entry<String, String>> getErrors() {
        return errorList;
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
