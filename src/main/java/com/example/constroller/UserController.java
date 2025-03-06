package com.example.constroller;

import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.Map;

import com.example.model.User;
import com.example.model.UserModel;

public class UserController {
    private UserModel model = new UserModel();

    public void addUser(String emailAccount, String password) {
        User newUser = new User(emailAccount, BCrypt.hashpw(password, BCrypt.gensalt()));
        model.addUser(newUser);
    }

    // remove, update

    public List<Map.Entry<String, String>> getInputErrors() {
        return model.getErrors();
    }
}
