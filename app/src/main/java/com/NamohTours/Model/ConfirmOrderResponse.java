package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ConfirmOrderResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String warning;

    @SerializedName("data")
    private ConfirmOrderDetailResponse response;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getWarning() {
        return warning;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getError_description() {
        return error_description;
    }

    public ConfirmOrderDetailResponse getResponse() {
        return response;
    }
}
