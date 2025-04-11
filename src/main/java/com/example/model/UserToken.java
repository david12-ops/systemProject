package com.example.model;

public class UserToken {

    private final String mailAccount;
    private final String groupId;
    private final String userId;

    public UserToken(String userId, String groupId, String mailAccount) {
        this.userId = userId;
        this.groupId = groupId;
        this.mailAccount = mailAccount;
    }

    public String getMailAccount() {
        return mailAccount;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }
}
