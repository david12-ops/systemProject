package com.example.components;

import com.example.constroller.UserController;
import com.example.model.UserToken;

import javafx.scene.image.Image;

public class Avatar {

    public Avatar(UserController userController, UserToken userToken) {
        Image userImage = userController.getImageProfile(userToken);
    }
}
