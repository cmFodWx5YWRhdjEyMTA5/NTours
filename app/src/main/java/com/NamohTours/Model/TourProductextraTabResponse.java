package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 7/7/17.
 */

public class TourProductextraTabResponse {

    @SerializedName("heading")
    private String heading;

    @SerializedName("description")
    private String description;


    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }


}
