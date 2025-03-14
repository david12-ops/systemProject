package utils;

import com.example.model.User;

public interface AuthManagement {

    boolean register(String emailAccount, String password);

    boolean login(String emailAccount, String password);

    void logOut();

    User getLoggedUser();

    boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword);

}
