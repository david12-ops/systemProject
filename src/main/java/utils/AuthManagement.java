package utils;

import com.example.model.User;

public interface AuthManagement {

    User register(String emailAccount, String password);

    User login(String emailAccount, String password);

    void logOut();

    User getLoggedUser();

    User updateNotLoggedAccount(String emailAccount, String password, String newPassword, String confirmationPassword);

}
