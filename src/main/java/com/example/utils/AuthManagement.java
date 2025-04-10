package com.example.utils;

public interface AuthManagement {

    boolean register(String emailAccount, String password);

    void login(String emailAccount, String password);

    void logOut();

    UserToken getLoggedUser();

    boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword);

}
