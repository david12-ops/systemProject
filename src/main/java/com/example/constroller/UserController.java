package com.example.constroller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.example.model.User;
import com.example.model.UserModel;
import com.example.model.UserToken;
import com.example.utils.ImageConvertor;
import com.example.utils.SessionHolder;
import com.example.utils.enums.AddTypeOperation;
import com.example.utils.enums.GetUserTypeOperation;
import com.example.utils.interfaces.AuthManagement;
import com.example.utils.interfaces.UserManagement;
import com.example.utils.services.SessionService;

import javafx.scene.image.Image;

public class UserController implements AuthManagement, UserManagement {
    private UserModel model;
    private SessionService service;

    // TODOD - support switching - yes - implement group Id - need gui
    // TODO - app bar - (logo - search bar - Avatar (drop menu) - log out)

    public UserController(UserModel model) {
        this.model = model;
        this.service = SessionService.getInstance();
    }

    private User getUser(String emailAccount, String password, GetUserTypeOperation getUserTypeOperation) {
        if (getUserTypeOperation == GetUserTypeOperation.CURRENT) {
            UserToken userToken = getLoggedUser();
            return model.getUserByCredentials(null, null, userToken);
        }

        if (getUserTypeOperation == GetUserTypeOperation.BYCREDENTIALS) {
            return model.getUserByCredentials(emailAccount, password, null);
        }
        return null;
    }

    public HashMap<String, String> getInputErrors() {
        return model.getErrors();
    }

    // Auth
    @Override
    public boolean register(String emailAccount, String password) {
        model.addUser(emailAccount, password, null, AddTypeOperation.NEWACCOUNT);
        return getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS) != null ? true : false;
    }

    @Override
    public void login(String emailAccount, String password) {
        User user = getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS);

        if (user != null && !this.service.isUserLoggedIn(user.getUserId())) {
            String sessionId = this.service.createSessionId(user);
            SessionHolder.setSessionId(sessionId);
        }
    }

    @Override
    public boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword) {

        User foundUser = getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS);

        model.updateUser(foundUser, newPassword, confirmationPassword);
        return getUser(emailAccount, confirmationPassword, GetUserTypeOperation.BYCREDENTIALS) != null ? true : false;
    }

    @Override
    public void logOut() {
        String sessionId = SessionHolder.getSessionId();
        if (sessionId != null) {
            this.service.removeSession(sessionId);
            SessionHolder.clear();
        }
    }

    @Override
    public UserToken getLoggedUser() {
        try {
            String sessionId = SessionHolder.getSessionId();
            if (sessionId != null && !sessionId.isBlank()) {
                return this.service.getUserBySessionId(sessionId);
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching logged user: " + e);
            return null;
        }
    }

    @Override
    public Image getImageProfile() {
        User user = getUser(null, null, GetUserTypeOperation.CURRENT);

        if (user != null && user.getProfileImage() != null) {
            return ImageConvertor.Base64ToImage(user.getProfileImage());
        }
        return null;
    }

    // User actions
    @Override
    public void removeAccount(User user) {
        UserToken userToken = getLoggedUser();
        model.removeUser(userToken, user);
    }

    @Override
    public boolean updateLoggedInAccount(String newPassword, String confirmationPassword) {
        UserToken userToken = getLoggedUser();
        System.out.println(userToken.getMailAccount());

        model.updateUser(userToken, newPassword, confirmationPassword);
        return getUser(userToken.getMailAccount(), confirmationPassword, GetUserTypeOperation.BYCREDENTIALS) != null
                ? true
                : false;
    }

    @Override
    public void AddImageProfile(File file) {
        try {
            UserToken userToken = getLoggedUser();
            String base64 = ImageConvertor.imageToBase64(file);
            model.updateUser(userToken, base64);
        } catch (IOException e) {
            System.err.println("Error converting image: " + e.getMessage());
            // Optionally show Alert dialog here
        }
    }

    @Override
    public void addAnotherAccount(String emailAccount, String password) {
        UserToken userToken = getLoggedUser();
        model.addUser(emailAccount, password, userToken, AddTypeOperation.ANOTHERACCOUNT);
    }

    @Override
    public void switchAccount(User switchtoUser) {
        UserToken userToken = getLoggedUser();

        if (userToken != null && switchtoUser != null
                && !userToken.getMailAccount().equals(switchtoUser.getMailAccount())) {
            SessionHolder.setSessionId(null);
            String sessionId = this.service.createSessionId(switchtoUser);
            SessionHolder.setSessionId(sessionId);

            // after this view - screenController will reload and active mainScreen with new
            // session
        }
    }

    @Override
    public List<User> getAllUserAccounts() {
        UserToken userToken = getLoggedUser();
        return model.getAllUserAccounts(userToken);
    }
}
