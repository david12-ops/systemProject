package com.example.constroller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.model.User;
import com.example.model.UserModel;
import com.example.model.UserToken;
import com.example.utils.ImageConvertor;
import com.example.utils.SessionHolder;
import com.example.utils.enums.AddTypeOperation;
import com.example.utils.enums.GetUserTypeOperation;
import com.example.utils.interfaces.AuthManagement;
import com.example.utils.interfaces.ErrorManagement;
import com.example.utils.interfaces.UserManagement;
import com.example.utils.services.SessionService;

import javafx.scene.image.Image;

public class UserController implements AuthManagement, UserManagement {
    private UserModel userModel;
    private SessionService sessionService;

    // TODOD - support switching - yes - implement group Id - need gui

    public UserController(UserModel userModel) {
        this.userModel = userModel;
        this.sessionService = SessionService.getInstance();
    }

    private User getUser(String emailAccount, String password, GetUserTypeOperation getUserTypeOperation) {
        if (getUserTypeOperation == GetUserTypeOperation.CURRENT) {
            UserToken userToken = getLoggedUser();
            return userModel.getUserByCredentials(null, null, userToken);
        }

        if (getUserTypeOperation == GetUserTypeOperation.BYCREDENTIALS) {
            return userModel.getUserByCredentials(emailAccount, password, null);
        }
        return null;
    }

    public ErrorManagement getErrorHandler() {
        return userModel.getErrorHandler();
    }

    // Auth
    @Override
    public boolean register(String emailAccount, String password, String confirmationPassword) {
        userModel.addUser(emailAccount, password, confirmationPassword, null, AddTypeOperation.NEWACCOUNT);
        return getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS) != null ? true : false;
    }

    @Override
    public void login(String emailAccount, String password) {
        User user = getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS);

        if (user != null && !this.sessionService.isUserLoggedIn(user.getUserId())) {
            String sessionId = this.sessionService.createSessionId(user);
            SessionHolder.setSessionId(sessionId);
        }
    }

    @Override
    public boolean updateNotLoggedAccount(String emailAccount, String password, String newPassword,
            String confirmationPassword) {

        User foundUser = getUser(emailAccount, password, GetUserTypeOperation.BYCREDENTIALS);

        return userModel.updateUser(foundUser, newPassword, confirmationPassword);
    }

    @Override
    public void logOut() {
        String sessionId = SessionHolder.getSessionId();
        if (sessionId != null) {
            this.sessionService.removeSession(sessionId);
            SessionHolder.clear();
        }
    }

    @Override
    public UserToken getLoggedUser() {
        try {
            String sessionId = SessionHolder.getSessionId();
            if (sessionId != null && !sessionId.isBlank()) {
                return this.sessionService.getUserBySessionId(sessionId);
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
        userModel.removeUser(userToken, user);
    }

    @Override
    public boolean updateLoggedInAccount(String newPassword, String confirmationPassword) {
        UserToken userToken = getLoggedUser();
        return userModel.updateUser(userToken, newPassword, confirmationPassword);
    }

    @Override
    public void updateImageProfile(File file) {
        try {
            UserToken userToken = getLoggedUser();
            if (file == null) {
                userModel.updateUser(userToken, null);

            } else {
                String base64 = ImageConvertor.imageToBase64(file);
                userModel.updateUser(userToken, base64);
            }
        } catch (IOException e) {
            System.err.println("Error converting image: " + e.getMessage());
        }
    }

    @Override
    public void addAnotherAccount(String emailAccount, String password, String confirmationPassword) {
        UserToken userToken = getLoggedUser();
        userModel.addUser(emailAccount, password, confirmationPassword, userToken, AddTypeOperation.ANOTHERACCOUNT);
    }

    @Override
    public boolean switchAccount(User switchtoUser) {
        UserToken userToken = getLoggedUser();

        if (userToken != null && switchtoUser != null
                && !userToken.getMailAccount().equals(switchtoUser.getMailAccount())) {
            logOut();
            String sessionId = this.sessionService.createSessionId(switchtoUser);
            SessionHolder.setSessionId(sessionId);
            return true;

            // after this view - screenController will reload and active mainScreen with new
            // session
        }

        return false;
    }

    @Override
    public List<User> getAllUserAccounts() {
        UserToken userToken = getLoggedUser();
        return userModel.getAllUserAccounts(userToken);
    }
}
