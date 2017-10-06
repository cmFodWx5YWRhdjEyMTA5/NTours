package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 5/6/17.
 */

public class TourProductResponseById {


    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private TourProductDetailResponse data;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public String getError() {
        return error;
    }

    public TourProductDetailResponse getData() {
        return data;
    }

    public void setData(TourProductDetailResponse data) {
        this.data = data;
    }


}
