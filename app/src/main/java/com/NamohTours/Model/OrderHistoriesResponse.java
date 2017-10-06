package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class OrderHistoriesResponse {

    @SerializedName("notify")
    private String notify;

    @SerializedName("status")
    private String status;

    @SerializedName("comment")
    private String comment;

    @SerializedName("date_added")
    private String date_added;

    public String getNotify() {
        return notify;
    }


    public String getStatus() {
        return status;
    }


    public String getComment() {
        return comment;
    }


    public String getDate_added() {
        return date_added;
    }


}
