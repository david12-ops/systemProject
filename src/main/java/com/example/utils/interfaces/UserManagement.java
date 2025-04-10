package com.example.utils.interfaces;

import com.example.model.User;
import com.example.model.UserToken;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;

public interface UserManagement {

    void addAccount(String emailAccount, String password);

    void removeAccount(User user);

    boolean updateLoggedInAccount(String newPassword, String confirmationPassword);

    Image getImageProfile(UserToken userToken);

    void AddImageProfile(File file, UserToken userToken) throws IOException;
}
