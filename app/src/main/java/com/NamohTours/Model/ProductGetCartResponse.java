package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class ProductGetCartResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String warning;

    @SerializedName("data")
    private ProductGetCartDetailResponse data;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    public ProductGetCartDetailResponse getData() {
        return data;
    }

    public void setData(ProductGetCartDetailResponse data) {
        this.data = data;
    }

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


}
