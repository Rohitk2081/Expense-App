package com.kehgye.expensetrackingapp;

import com.google.gson.annotations.SerializedName;

public class Expense {
    private String userId;
    private String title;
    private double amount;
    private String description;
    private String date;
    @SerializedName("_id")
    private String expenseId;



    // Constructor for adding a new expense without specifying date (handled by backend)
    public Expense(String userId, String title, double amount, String description) {
        this.userId = userId;
        this.title = title;
        this.amount = amount;
        this.description = description;
    }

    public Expense(String title, double amount, String description) {
        this.title = title;
        this.amount = amount;
        this.description = description;
    }

    // Getters and setters
    public String getExpenseId() {
        return expenseId;
    }

    // Setter method for expenseId (optional)
    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}