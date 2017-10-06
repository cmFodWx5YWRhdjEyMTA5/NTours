package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 8/9/17.
 */

public class PostUserPaymentAddressResponse {

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

    @SerializedName("address_id")
    private String address_id;

    @SerializedName("payment_address")
    private String payment_address;


    public PostUserPaymentAddressResponse(String payment_address, String address_id) {
        this.payment_address = payment_address;
        this.address_id = address_id;
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

    public String getError_description() {
        return error_description;
    }


    public String getAddress_id() {
        return address_id;
    }

    public String getPayment_address() {
        return payment_address;
    }

    public String getWarning() {
        return warning;
    }
}
