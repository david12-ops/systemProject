package components;

import com.example.constroller.UserController;

import javafx.scene.image.Image;
import utils.UserToken;

public class Avatar {

    public Avatar(UserController userController, UserToken userToken) {
        Image userImage = userController.getImageProfile(userToken);
    }
}
