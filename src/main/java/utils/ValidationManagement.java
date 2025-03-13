package utils;

import java.util.List;

import com.example.model.User;

import utils.Enums.Operation;

public interface ValidationManagement {

    boolean validateUserData(String email, String password);

    boolean validateMessageData(String sender, String acceptor);

    boolean nonDuplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users);

    boolean confirmedPassword(String newPassword, String confirmNewPassword);
}
