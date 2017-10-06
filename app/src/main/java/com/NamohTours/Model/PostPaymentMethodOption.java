package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class PostPaymentMethodOption {


    @SerializedName("success")
    private String success;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("error")
    private String warning;

    @SerializedName("payment_method")
    private String payment_method;

    @SerializedName("agree")
    private String agree;

    @SerializedName("comment")
    private String comment;


    public PostPaymentMethodOption(String payment_method, String agree) {
        this.payment_method = payment_method;
        this.agree = agree;
    }

    public String getSuccess() {
        return success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getAgree() {
        return agree;
    }

    public String getComment() {
        return comment;
    }

    public String getWarning() {
        return warning;
    }
}
