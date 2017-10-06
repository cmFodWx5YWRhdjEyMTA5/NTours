package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Pooja Mantri on 6/10/17.
 */

public class GetUserUploadDocumentsByOrderIdResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("order")
    private String order;

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


    public GetUserUploadDocumentsByOrderIdResponse(String order) {
        this.order = order;
    }

    public Boolean getSuccess() {
        return success;
    }


    public String getOrder() {
        return order;
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
