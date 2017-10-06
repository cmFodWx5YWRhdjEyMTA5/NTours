package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 7/9/17.
 */

public class GetUserPaymentAddress {


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

    @SerializedName("data")
    private GetUserPaymentAddressResponse data;


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

    public GetUserPaymentAddressResponse getData() {
        return data;
    }

    public String getWarning() {
        return warning;
    }
}
