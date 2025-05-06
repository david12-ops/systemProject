package com.example.utils.interfaces;

import com.example.model.User;

import java.io.File;
import java.io.IOException;

public interface UserManagement {

    void addAnotherAccount(String emailAccount, String password, String confirmationPassword);

    boolean switchAccount(User switchtoUser);

    boolean removeAccount(User user);

    boolean updateLoggedInAccount(String newPassword, String confirmationPassword);

    void updateImageProfile(File file) throws IOException;
}
