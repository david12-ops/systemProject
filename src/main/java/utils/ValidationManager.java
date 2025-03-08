package utils;

import java.util.List;

import com.example.model.User;

import utils.Enums.Operation;

public interface ValidationManager {

    public void validateUserData(String email, String password);

    public void validateMessageData(String sender, String acceptor);

    public void duplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users);
}
