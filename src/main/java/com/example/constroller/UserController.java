package com.example.constroller;

import org.mindrot.jbcrypt.BCrypt;

import com.example.model.User;
import com.example.model.UserModel;

public class UserController {
    private UserModel model = new UserModel();

    public void addAccount(String emailAccount, String password) {
        User newUser = new User(emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
        model.addUser(newUser);
    }

    // public void removeAccount(String emailAccount) {
    // }

    // public void updateAccount(String emailAccount, String password) {
    // }

    // public List<Map.Entry<String, String>> getInputErrors() {
    // return model.getErrors();
    // }
}
