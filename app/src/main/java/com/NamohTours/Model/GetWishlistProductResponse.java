package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class GetWishlistProductResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;


    @SerializedName("data")
    private GetWishlistProductDetailResponse wishLisDetailResponse;

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

    public GetWishlistProductDetailResponse getWishLisDetailResponse() {
        return wishLisDetailResponse;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
