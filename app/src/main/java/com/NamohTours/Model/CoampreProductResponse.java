package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class CoampreProductResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;


    @SerializedName("data")
    private Map<String, ComapreProductDetailResponse> wishLisDetailResponse;

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

    public Map<String, ComapreProductDetailResponse> getWishLisDetailResponse() {
        return wishLisDetailResponse;
    }

    public void setWishLisDetailResponse(Map<String, ComapreProductDetailResponse> wishLisDetailResponse) {
        this.wishLisDetailResponse = wishLisDetailResponse;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
