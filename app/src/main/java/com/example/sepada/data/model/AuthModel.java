package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthModel implements Serializable {
    @SerializedName("code")
    Integer code;
    @SerializedName("message")
    String message;
    @SerializedName("user_id")
    String userId;
    @SerializedName("role")
    String role;
    @SerializedName("username")
    String username;

    public AuthModel(Integer code, String message, String userId, String role, String username) {
        this.code = code;
        this.message = message;
        this.userId = userId;
        this.role = role;
        this.username = username;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}


