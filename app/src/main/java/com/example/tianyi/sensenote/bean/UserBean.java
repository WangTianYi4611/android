package com.example.tianyi.sensenote.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String userName;
    private String userPassWord;
    private String userChineseName;
    private String email;
    private String token;

    public UserBean(String username, String password, String email) {
        this.userName = username;
        this.userPassWord = password;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserChineseName() {
        return userChineseName;
    }

    public void setUserChineseName(String userChineseName) {
        this.userChineseName = userChineseName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }
}
