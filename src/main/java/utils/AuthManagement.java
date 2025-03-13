package utils;

import com.example.model.User;

public interface AuthManagement {

    void register(String emailAccount, String password);

    void login(String emailAccount, String password);

    void logOut();

    User getLoggedUser();

    void updateNotLoggedAccount(String emailAccount, String password, String newPassword, String confirmationPassword);

}
