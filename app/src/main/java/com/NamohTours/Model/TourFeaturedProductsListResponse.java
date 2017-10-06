package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 21/6/17.
 */

public class TourFeaturedProductsListResponse {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("name")
    private String name;

    @SerializedName("price_excluding_tax")
    private String price_excluding_tax;

    @SerializedName("price")
    private String price;

    @SerializedName("price_formated")
    private String price_formated;

    @SerializedName("special")
    private String special;

    @SerializedName("rating")
    private String rating;

    @SerializedName("description")
    private String description;


    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getThumb() {
        return thumb;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecial() {
        return special;
    }


    public String getPrice_formated() {
        return price_formated;
    }


}

