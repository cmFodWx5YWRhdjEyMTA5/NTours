package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 16/5/17.
 */

public class SlideShowResponse {

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<SlideShowDetailResponse> slideShowDetailResponses;

    public String getSuccess() {
        return success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public List<SlideShowDetailResponse> getSlideShowDetailResponses() {
        return slideShowDetailResponses;
    }
}
