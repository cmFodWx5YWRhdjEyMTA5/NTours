package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ubuntu on 5/6/17.
 */

public class TourProductOptionsResponse implements Serializable {


    @SerializedName("product_option_id")
    private String product_option_id;


    @SerializedName("option_value")
    private List<TourProductOptionsDetailResponse> tourProductOptionsDetailResponseList;


    @SerializedName("option_id")
    private String option_id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    @SerializedName("required")
    private String required;


    public String getProduct_option_id() {
        return product_option_id;
    }


    public String getOption_id() {
        return option_id;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public List<TourProductOptionsDetailResponse> getTourProductOptionsDetailResponseList() {
        return tourProductOptionsDetailResponseList;
    }

    public String getRequired() {
        return required;
    }

    public String getValue() {
        return value;
    }


}
