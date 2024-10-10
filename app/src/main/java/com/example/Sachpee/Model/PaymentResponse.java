package com.example.Sachpee.Model;

// PaymentResponse.java
import com.google.gson.annotations.SerializedName;

public class PaymentResponse {
    @SerializedName("return_code")
    private int returnCode;

    @SerializedName("return_message")
    private String returnMessage;

    @SerializedName("app_trans_id")
    private String appTransId;

    @SerializedName("amount")
    private int amount;

    @SerializedName("order_url")
    private String orderUrl;

    // Các trường khác tùy theo phản hồi của ZaloPay

    // Getters và Setters
    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getAppTransId() {
        return appTransId;
    }

    public void setAppTransId(String appTransId) {
        this.appTransId = appTransId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    // Các getter và setter khác nếu cần
}

