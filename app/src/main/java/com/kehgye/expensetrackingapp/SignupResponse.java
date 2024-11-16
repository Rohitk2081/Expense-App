package com.kehgye.expensetrackingapp;

public class SignupResponse {
    private String message;  // Message from the server (e.g., "Signup successful")
    private String username;  // Optional: username or user ID (if returned)
    private boolean success;  // Optional: status of the signup process

    // Getter and setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}