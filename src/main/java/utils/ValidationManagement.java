package utils;

import java.util.List;

import com.example.model.User;

import utils.Enums.Operation;

public interface ValidationManagement {

    public boolean validateUserData(String email, String password);

    public boolean validateMessageData(String sender, String acceptor);

    public boolean nonDuplicateUserWithEmail(Operation operation, String senderUserEm, List<User> users);
}
