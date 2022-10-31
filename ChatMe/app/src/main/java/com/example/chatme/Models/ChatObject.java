package com.example.chatme.Models;

public class ChatObject {
    private String username;
    private String fullName;
    private String message;
    private String avt;

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatObject() {
    }

    public ChatObject(String username, String fullName, String message, String avt) {
        this.username= username;
        this.fullName = fullName;
        this.message = message;
        this.avt= avt;
    }

    @Override
    public String toString() {
        return "ChatObject{" +
                "fullName='" + fullName + '\'' +
                ", message='" + message + '\'' +
                ", avt='" + avt + '\'' +
                '}';
    }
}
