package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 8/9/17.
 */

public class GetPaymentMethodResponse {

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
    private GetPaymentMethodDetailResponse paymentMethodDetailResponse;


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

    public GetPaymentMethodDetailResponse getPaymentMethodDetailResponse() {
        return paymentMethodDetailResponse;
    }

    public String getWarning() {
        return warning;
    }
}
