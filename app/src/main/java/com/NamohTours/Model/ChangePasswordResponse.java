package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 8/6/17.
 */

public class ChangePasswordResponse {


    @SerializedName("success")
    private Boolean success;

    @SerializedName("error")
    private ChangePasswordFields warning;


    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;


    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }


    public String getStatusCode() {
        return statusCode;
    }

    public String getError_description() {
        return error_description;
    }

    public String getStatusText() {
        return statusText;
    }

    public ChangePasswordFields getWarning() {
        return warning;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setWarning(ChangePasswordFields warning) {
        this.warning = warning;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }


}
