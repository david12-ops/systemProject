package com.example.constroller;

import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import utilities.JsonStorage;

public class SignInManagerController {

    public static boolean register(String username, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'createEmptyList'");
    }

    public static boolean login(String username, String password) {
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
