package com.example.sepada.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseModel implements Serializable {

    @SerializedName("code")
    Integer code;
    @SerializedName("message")
    String message;
    @SerializedName("day")
    private String day;

    public ResponseModel(Integer code, String day, String message) {
        this.code = code;
        this.message = message;
        this.day = day;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
