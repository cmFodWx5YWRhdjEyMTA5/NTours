package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 23/5/17.
 */

public class UserAccountRewardsDetails {

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("points")
    private String points;

    @SerializedName("description")
    private String description;

    @SerializedName("date_added")
    private String date_added;


    public String getOrder_id() {
        return order_id;
    }

    public String getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }

    public String getDate_added() {
        return date_added;
    }
}