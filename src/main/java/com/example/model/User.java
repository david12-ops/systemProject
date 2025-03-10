package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class User {

    @JsonProperty("user_id")
    private String id;

    @JsonProperty("mailAccount")
    private String mailAccount;

    @JsonProperty("password")
    private String password;

    public User() {
    }

    public User(String id, String mailAccount, String password) {
        this.mailAccount = mailAccount;
        this.password = password;
        this.id = id;
    }

    public User(String mailAccount, String password) {
        this.mailAccount = mailAccount;
        this.password = password;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return mailAccount.equals(user.mailAccount);
    }

    public String getMailAccount() {
        return this.mailAccount;
    }

    public String getPassword() {
        return this.password;
    }

    public void setMailAccount(String newMail) {
        this.mailAccount = newMail;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
