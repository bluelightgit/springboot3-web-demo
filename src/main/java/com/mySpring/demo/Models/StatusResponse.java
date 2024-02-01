package com.mySpring.demo.Models;

public class StatusResponse {
    private int status;
    private String message;

    // getters and setters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
