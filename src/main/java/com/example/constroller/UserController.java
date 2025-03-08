package com.example.constroller;

import com.example.model.UserModel;

public class UserController {
    private UserModel model = new UserModel();

    public void addAccount(String emailAccount, String password) {
        model.addUser(emailAccount, password);
    }

    public void removeAccount() {
        model.removeUser();
    }

    public void updateAccount(String emailAccount, String password) {
    }

    public String getInputErrors() {
        return model.getErrors();
    }
}
