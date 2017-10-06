package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 26/5/17.
 */

public class TourCategoryFilterResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private TourCategoryDetailResponse tourCategoryDetailResponses;

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

    public TourCategoryDetailResponse getTourCategoryDetailResponses() {
        return tourCategoryDetailResponses;
    }

    public String getError_description() {
        return error_description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getError() {
        return error;
    }
}
