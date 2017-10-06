package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 9/5/17.
 */

public class TourProductResponse {


    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private List<TourProductDetailResponse> data;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public String getError() {
        return error;
    }

    public List<TourProductDetailResponse> getData() {
        return data;
    }

    public void setData(List<TourProductDetailResponse> data) {
        this.data = data;
    }
}
