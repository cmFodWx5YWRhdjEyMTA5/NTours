package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 21/6/17.
 */

public class ForgetPasswordResponse {
    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private Object warning;


    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;


    private String email;

    public ForgetPasswordResponse(String email) {
        this.email = email;
    }


    public String getSuccess() {
        return success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Object getWarning() {
        return warning;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getError_description() {
        return error_description;
    }
}
