package com.example.constroller;

public class SignInManagerController {
    UserController controller = new UserController();
    // udelat nejky trvaly token pro usera

    public void register(String username, String password) {
        controller.addAccount(username, password);
    }

    public void login(String username, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyList'");
    }

    public static void logOut() {
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyList'");
    }

    /*
     * Crypt.checkpw(password, user.getHashedPassword()) ^ - Start of the string.
     * (?=.*[A-Z]) - At least one uppercase letter. (?=.*[a-z]) - At least one
     * lowercase letter. (?=.*\\d) - At least one digit. (?=.*[@$!%*?&]) - At least
     * one special character (e.g., @, $, !, %, *, ?, &). [A-Za-z\\d@$!%*?&]{8,} -
     * Password must be at least 8 characters long and can contain uppercase
     * letters, lowercase letters, digits, and special characters. $ - End of the
     * string.
     */

}
