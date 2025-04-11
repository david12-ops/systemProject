package com.example.components;

import com.example.constroller.UserController;

import javafx.scene.image.Image;

public class Avatar {

    public Avatar(UserController userController) {
        Image userImage = userController.getImageProfile();
    }
}
