package com.example.constroller;

import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import com.example.model.User;
import com.example.model.UserModel;

import utils.AuthManagement;
import utils.SessionService;
import utils.UserManagement;

public class UserController implements AuthManagement, UserManagement {
    private UserModel model;
    private SessionService service;
    private String sessionId;
    // TODO - while removing user, need info about removing accout

    public UserController(UserModel model) {
        this.model = model;
        this.service = SessionService.getInstance();
    }

    private User getUser(String emailAccount, String password) {
        return model.getUserByCredentials(emailAccount, password);
    }

    public HashMap<String, String> getInputErrors() {
        return model.getErrors();
    }

    // Auth
    @Override
    public void register(String emailAccount, String password) {
        model.addUser(emailAccount, password);
    }

    @Override
    public void login(String emailAccount, String password) {
        User user = getUser(emailAccount, password);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            if (this.service.getUserBySessionId(user.getMailAccount()) == null) {
                this.sessionId = this.service.createSessionId(user);
            }
        }
    }

    @Override
    public void updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword) {

        User currUser = getUser(emailAccount, password);
        User updatedUser = new User(emailAccount, password);

        if (currUser != null) {
            model.updateUser(currUser, updatedUser);
        }
    }

    @Override
    public void logOut() {
        this.service.removeSession(sessionId);
    }

    @Override
    public User getLoggedUser() {
        return this.service.getUserBySessionId(sessionId);
    }

    // User actions
    @Override
    public void addAccount(String emailAccount, String password) {
        model.addUser(emailAccount, password);
    }

    @Override
    public void removeAccount(User user) {
        model.removeUser(getLoggedUser(), user);
    }

    @Override
    public void updateLoggedInAccount(String emailAccount, String password, String newPassword) {

        User loggedUser = getLoggedUser();
        User updatedUser = new User(emailAccount, password);

        if (loggedUser != null) {
            model.updateUser(loggedUser, updatedUser);
        }

        if (getUser(updatedUser.getMailAccount(), updatedUser.getPassword()) != null) {
            loggedUser.setMailAccount(emailAccount);
            loggedUser.setPassword(password);
        }

    }
}
