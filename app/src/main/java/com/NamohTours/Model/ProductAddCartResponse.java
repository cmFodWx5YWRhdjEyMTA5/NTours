package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Pooja Mantri on 29/8/17.
 */

public class ProductAddCartResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private ProductAddCartDetails warning;

    @SerializedName("data")
    private ProductAddCartDetails data;


    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    public ProductAddCartDetails getData() {
        return data;
    }

    public void setData(ProductAddCartDetails data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ProductAddCartDetails getWarning() {
        return warning;
    }

    public void setWarning(ProductAddCartDetails warning) {
        this.warning = warning;
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
    // following are the response for user register


}
