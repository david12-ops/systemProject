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

    @JsonProperty("profileImage")
    private String profileImage;

    public User() {
    }

    public User(String id, String mailAccount, String password) {
        this.mailAccount = mailAccount;
        this.password = password;
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.profileImage = null;
    }

    /*
     * This method determines how the objects will be compared.
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return id.equals(user.id);
    }

    public String getId() {
        return id;
    }

    public String getMailAccount() {
        return mailAccount;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setMailAccount(String newMail) {
        mailAccount = newMail;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public void setImage(String image) {
        profileImage = image;
    }
}
