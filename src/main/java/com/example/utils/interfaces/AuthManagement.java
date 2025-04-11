package com.example.utils.interfaces;

import java.util.List;

import com.example.model.User;
import com.example.model.UserToken;

import javafx.scene.image.Image;

public interface AuthManagement {

    boolean register(String emailAccount, String password);

    void login(String emailAccount, String password);

    void logOut();

    UserToken getLoggedUser();

    List<User> getAllUserAccounts();

    boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword);

    Image getImageProfile();
}
