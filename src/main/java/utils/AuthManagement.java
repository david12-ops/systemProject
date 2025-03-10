package utils;

import com.example.model.User;

public interface AuthManagement {

    public void register(String emailAccount, String password);

    public void login(String emailAccount, String password);

    public void logOut();

    public User getLoggedUser();
}
