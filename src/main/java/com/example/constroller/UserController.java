package com.example.constroller;

import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import com.example.model.User;
import com.example.model.UserModel;

import utils.AuthManagement;
import utils.SessionContext;
import utils.SessionService;
import utils.UserManagement;
import utils.UserToken;

public class UserController implements AuthManagement, UserManagement {
    private UserModel model;
    private SessionService service;

    // In this user cannot update email adress only password of account
    // TODO - when messages will be done - can add functionality to update email
    // adress of account
    // TODO - SessionContext and token move to another package (in utils right now)

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
    public boolean register(String emailAccount, String password) {
        model.addUser(emailAccount, password);
        return getUser(emailAccount, password) != null ? true : false;
    }

    @Override
    public void login(String emailAccount, String password) {
        User user = getUser(emailAccount, password);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            if (!this.service.isUserLoggedIn(user.getId())) {
                String sessionId = this.service.createSessionId(user);
                SessionContext.setSessionId(sessionId);
            }
        }
    }

    @Override
    public boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword) {

        User foundUser = getUser(emailAccount, password);

        model.updateUser(foundUser, newPassword, confirmationPassword);
        return getUser(emailAccount, confirmationPassword) != null ? true : false;
    }

    @Override
    public void logOut() {
        String sessionId = SessionContext.getSessionId();
        if (sessionId != null) {
            this.service.removeSession(sessionId);
            SessionContext.clear();
        }
    }

    @Override
    public UserToken getLoggedUser() {
        try {
            String sessionId = SessionContext.getSessionId();
            if (sessionId != null && !sessionId.isBlank()) {
                return this.service.getUserBySessionId(sessionId);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching logged user: " + e);
            return null;
        }
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
    public boolean updateLoggedInAccount(String newPassword, String confirmationPassword) {

        UserToken userToken = getLoggedUser();

        model.updateUser(userToken, newPassword, confirmationPassword);
        return getUser(userToken.getEmail(), confirmationPassword) != null ? true : false;
    }
}
