package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 16/5/17.
 */

public class SlideShowBannersResponse {


    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    @SerializedName("image")
    private String image;


    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }
}
