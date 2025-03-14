package utils;

import com.example.model.User;

public interface UserManagement {

    void addAccount(String emailAccount, String password);

    void removeAccount(User user);

    boolean updateLoggedInAccount(String newPassword, String confirmationPassword);
}
