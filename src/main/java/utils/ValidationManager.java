package utils;

public interface ValidationManager {

    public void validateUserData(String email, String password);

    public void validateMessageData(String sender, String acceptor);
}
