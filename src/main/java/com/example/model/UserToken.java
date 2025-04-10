package com.example.model;

public class UserToken {

    private final String email;

    private final String userId;

    public UserToken(String email, String userId) {
        this.email = email;
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getId() {
        return this.userId;
    }
}
