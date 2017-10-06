package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class OrderHistoryOptionResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    @SerializedName("type")
    private String type;

    @SerializedName("product_option_id")
    private String product_option_id;

    @SerializedName("product_option_value_id")
    private String product_option_value_id;

    @SerializedName("option_id")
    private String option_id;

    @SerializedName("option_value_id")
    private String option_value_id;

    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }


    public String getType() {
        return type;
    }


    public String getProduct_option_id() {
        return product_option_id;
    }


    public String getProduct_option_value_id() {
        return product_option_value_id;
    }

    public String getOption_id() {
        return option_id;
    }


    public String getOption_value_id() {
        return option_value_id;
    }


}
