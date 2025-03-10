package utils;

import com.example.model.User;

public interface UserManagement {

    void addAccount(String emailAccount, String password);

    void removeAccount(User user);

    void updateAccount(String emailAccount, String password);
}
