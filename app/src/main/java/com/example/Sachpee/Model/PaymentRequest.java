package com.example.Sachpee.Model;

// PaymentRequest.java
public class PaymentRequest {
    private String app_user;
    private int amount;
    private String description;

    public PaymentRequest(String app_user, int amount, String description) {
        this.app_user = app_user;
        this.amount = amount;
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApp_user() {
        return app_user;
    }

    public void setApp_user(String app_user) {
        this.app_user = app_user;
    }
// Getters và Setters nếu cần
}
