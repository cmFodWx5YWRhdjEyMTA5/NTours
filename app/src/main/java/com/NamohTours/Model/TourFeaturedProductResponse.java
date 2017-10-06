package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 21/6/17.
 */

public class TourFeaturedProductResponse {


    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<TourFeaturedProductDetailResponse> tourCategoryDetailResponses;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("error")
    private String error;


    public String getSuccess() {
        return success;
    }

    public String getStatusCode() {
        return statusCode;
    }


    public List<TourFeaturedProductDetailResponse> getTourCategoryDetailResponses() {
        return tourCategoryDetailResponses;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getError() {
        return error;
    }

    public String getError_description() {
        return error_description;
    }
}
