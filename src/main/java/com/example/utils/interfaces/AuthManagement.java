package com.example.utils.interfaces;

import com.example.model.UserToken;

public interface AuthManagement {

    boolean register(String emailAccount, String password);

    void login(String emailAccount, String password);

    void logOut();

    UserToken getLoggedUser();

    boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword);

}
