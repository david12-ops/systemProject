package com.example.constroller;

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
    // TODO - gui for auth
    // TODO - remove, update, chytrejsi erroring

    public UserController(UserModel model) {
        this.model = model;
        this.service = SessionService.getInstance();
    }

    private User getUser(String emailAccount, String password) {
        return model.getUserByCredentials(emailAccount, password);
    }

    public String getInputErrors() {
        return model.getErrors();
    }

    // Auth
    @Override
    public void register(String emailAccount, String password) {
        model.addUser(emailAccount, password);
        if (getUser(emailAccount, password) != null) {
            System.out.println("User registered successfully. Please log in.");
        } else {
            System.out.println("Registration failed");
        }
    }

    @Override
    public void login(String emailAccount, String password) {
        User user = getUser(emailAccount, password);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            if (this.service.getUserBySessionId(user.getMailAccount()) == null) {
                this.sessionId = this.service.createSessionId(user);
            } else {
                System.out.println("User is already logged in.");
            }
        } else {
            System.out.println("Invalid credentials.");
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
        if (getUser(emailAccount, password) != null) {
            System.out.println("User added successfully. Please log in.");
        } else {
            System.out.println("Addition failed");
        }
    }

    @Override
    public void removeAccount(User user) {
        if (getLoggedUser() == null || user == null) {
            System.out.println("Invalid operation: Active user or target user is null.");
        } else {
            model.removeUser(getLoggedUser(), user);
        }
    }

    @Override
    public void updateAccount(String emailAccount, String password) {
        User loggedUser = getLoggedUser();
        User updatedUser = new User(emailAccount, password);

        // TODO - update for no logged
        if (loggedUser != null) {
            User currUser = new User(loggedUser.getMailAccount(), loggedUser.getPassword());
            model.updateUser(currUser, updatedUser);

            if (getUser(updatedUser.getMailAccount(), updatedUser.getPassword()) != null) {
                loggedUser.setMailAccount(emailAccount);
                loggedUser.setPassword(password);
            }

        }
    }
}
