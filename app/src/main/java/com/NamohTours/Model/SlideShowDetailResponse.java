package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 16/5/17.
 */

public class SlideShowDetailResponse {

    @SerializedName("module_id")
    private Integer module_id;

    @SerializedName("name")
    private String name;

    @SerializedName("width")
    private String width;

    @SerializedName("height")
    private String height;

    @SerializedName("banners")
    List<SlideShowBannersResponse> slideShowBannersResponses;


    public Integer getModule_id() {
        return module_id;
    }

    public String getName() {
        return name;
    }


    public List<SlideShowBannersResponse> getSlideShowBannersResponses() {
        return slideShowBannersResponses;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public void setSlideShowBannersResponses(List<SlideShowBannersResponse> slideShowBannersResponses) {
        this.slideShowBannersResponses = slideShowBannersResponses;
    }


}


