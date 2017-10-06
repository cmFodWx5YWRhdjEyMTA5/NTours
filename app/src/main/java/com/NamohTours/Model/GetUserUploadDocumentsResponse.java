package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Pooja Mantri on 3/10/17.
 */

public class GetUserUploadDocumentsResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("cust")
    private String cust;

    @SerializedName("data")
    private HashMap<String, GetUserUploadDocumentsDetails> data;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("error")
    private String warning;


    public GetUserUploadDocumentsResponse(String cust) {
        this.cust = cust;
    }

    public Boolean getSuccess() {
        return success;
    }


    public String getCust() {
        return cust;
    }

    public HashMap<String, GetUserUploadDocumentsDetails> getData() {
        return data;
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

    public String getWarning() {
        return warning;
    }
}
