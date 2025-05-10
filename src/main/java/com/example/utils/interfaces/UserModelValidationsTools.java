package com.example.utils.interfaces;

import java.io.File;
import java.util.List;

import com.example.model.User;
import com.example.utils.enums.Form;
import com.example.utils.enums.Operation;

public interface UserModelValidationsTools {
    boolean validProfileImage(File profileImage);

    boolean validEmail(String email);

    boolean validPassword(String currentPassword, String password, String email, Form form);

    boolean nonDuplicateUserWithEmail(Operation operation, String senderEmail, List<User> users);

    boolean confirmedPassword(String password, String confirmationPassword, Form form);
}
