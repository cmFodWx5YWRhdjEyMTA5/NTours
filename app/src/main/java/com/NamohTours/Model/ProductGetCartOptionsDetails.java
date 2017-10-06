package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class ProductGetCartOptionsDetails {

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
