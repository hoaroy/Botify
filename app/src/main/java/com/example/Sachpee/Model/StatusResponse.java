package com.example.Sachpee.Model;

public class StatusResponse {
    private int return_code;
    private String return_message;
    private int sub_return_code;
    private String sub_return_message;
    private boolean is_processing;
    private int amount;
    private int zp_trans_id;
    private int discount_amount;

    // Getters and Setters
    public int getReturnCode() {
        return return_code;
    }

    public void setReturnCode(int return_code) {
        this.return_code = return_code;
    }

    public String getReturnMessage() {
        return return_message;
    }

    public void setReturnMessage(String return_message) {
        this.return_message = return_message;
    }

    public int getSubReturnCode() {
        return sub_return_code;
    }

    public void setSubReturnCode(int sub_return_code) {
        this.sub_return_code = sub_return_code;
    }

    public String getSubReturnMessage() {
        return sub_return_message;
    }

    public void setSubReturnMessage(String sub_return_message) {
        this.sub_return_message = sub_return_message;
    }

    public boolean isProcessing() {
        return is_processing;
    }

    public void setProcessing(boolean is_processing) {
        this.is_processing = is_processing;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getZpTransId() {
        return zp_trans_id;
    }

    public void setZpTransId(int zp_trans_id) {
        this.zp_trans_id = zp_trans_id;
    }

    public int getDiscountAmount() {
        return discount_amount;
    }

    public void setDiscountAmount(int discount_amount) {
        this.discount_amount = discount_amount;
    }
}
