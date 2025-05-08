package com.example.utils.interfaces;

import java.io.File;
import java.util.List;

import com.example.model.User;
import com.example.utils.enums.Operation;

public interface UserModelValidationsTools {
    boolean validProfileImage(File profileImage);

    boolean validEmailPassword(String email, String password);

    boolean nonDuplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users);

    boolean confirmedPassword(String newPassword, String confirmNewPassword);
}
