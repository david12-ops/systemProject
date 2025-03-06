package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("mailAccount")
    private String mailAccount;

    @JsonProperty("password")
    private String password;

    public User(String mailAccount, String password) {
        this.mailAccount = mailAccount;
        this.password = password;
    }

    public String getMailAccount() {
        return this.mailAccount;
    }

    public String getPassword() {
        return this.password;
    }
}
