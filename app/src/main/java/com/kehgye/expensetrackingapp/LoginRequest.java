package com.kehgye.expensetrackingapp;

public class LoginRequest {

    private String email;
    private String password;

    // Constructor to initialize email and password
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Optional: Setters (if needed)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}