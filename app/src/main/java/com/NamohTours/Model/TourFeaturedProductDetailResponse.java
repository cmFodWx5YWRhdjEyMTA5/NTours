package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 21/6/17.
 */

public class TourFeaturedProductDetailResponse {


    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("module_id")
    private String module_id;

    @SerializedName("products")
    private List<TourFeaturedProductsListResponse> featuredProductsListResponses;


    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getModule_id() {
        return module_id;
    }

    public List<TourFeaturedProductsListResponse> getFeaturedProductsListResponses() {
        return featuredProductsListResponses;
    }
}
