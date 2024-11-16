package com.kehgye.expensetrackingapp;

public class User {
    private String username;
    private String email; // Add email field
    private String password; // Changed from hashedPassword to password
    private String token; // JWT token after login

    // Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email; // Initialize email
        this.password = password; // Changed from hashedPassword
    }

    public User(String email, String password) {
        this.email = username;
        this.password = password; // Changed from hashedPassword
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { // Changed from getHashedPassword
        return password;
    }

    public void setPassword(String password) { // Changed from setHashedPassword
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' + // Include email in the toString for debugging
                ", token='" + (token != null ? "Token Present" : "No Token") + '\'' +
                '}';
    }
}