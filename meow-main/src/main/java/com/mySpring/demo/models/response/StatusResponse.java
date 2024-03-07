package com.mySpring.demo.models.response;

public class StatusResponse {
    private int status;
    private String message;
    private String token;

    public StatusResponse() {
    }

    public StatusResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusResponse(int status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    // getters and setters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
