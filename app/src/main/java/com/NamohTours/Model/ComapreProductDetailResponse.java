package com.NamohTours.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class ComapreProductDetailResponse {


    @SerializedName("product_id")
    private String product_id;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("price")
    private String price;

    @SerializedName("special")
    private String special;


    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return thumb;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecial() {
        return special;
    }
}
