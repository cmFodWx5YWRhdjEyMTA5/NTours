package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class GetUserOrderHistory {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;
    @SerializedName("error")
    private String warning;

    @SerializedName("data")
    private List<GetUserOrderHistoryDetail> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetUserOrderHistoryDetail> getData() {
        return data;
    }

    public void setData(List<GetUserOrderHistoryDetail> data) {
        this.data = data;
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
