package com.mrbattery.encounter.entity;

public class UserToken {
    private String userId;
    private String  token;

    public UserToken() {
    }

    public UserToken(String userId, String name, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
