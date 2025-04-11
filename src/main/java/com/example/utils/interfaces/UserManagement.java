package com.example.utils.interfaces;

import com.example.model.User;

import java.io.File;
import java.io.IOException;

public interface UserManagement {

    void addAnotherAccount(String emailAccount, String password);

    void switchAccount(User switchtoUser);

    void removeAccount(User user);

    boolean updateLoggedInAccount(String newPassword, String confirmationPassword);

    void AddImageProfile(File file) throws IOException;
}
