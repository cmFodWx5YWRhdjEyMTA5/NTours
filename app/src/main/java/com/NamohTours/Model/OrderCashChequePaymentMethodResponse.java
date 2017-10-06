package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 20/9/17.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderCashChequePaymentMethodResponse {

    @Expose
    @SerializedName("success")
    private Boolean success;

    @Expose
    @SerializedName("order_id")
    private Integer order_id;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("error")
    private String error;


    public OrderCashChequePaymentMethodResponse(String type) {

        this.type = type;

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
