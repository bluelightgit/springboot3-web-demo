package com.mySpring.demo.models.user.dtos;

import com.mysql.cj.log.Log;

public class LoginRequest {
    private String username;
    private String password;

    LoginRequest() {
    }

    LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters and setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
