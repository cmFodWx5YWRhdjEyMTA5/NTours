package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 5/6/17.
 */

public class TourProductOptionsDetailResponse {


    @SerializedName("image")
    private String image;

    @SerializedName("price")
    private String price;

    @SerializedName("price_excluding_tax")
    private String price_excluding_tax;

    @SerializedName("price_formated")
    private String price_formated;

    @SerializedName("product_option_value_id")
    private String product_option_value_id;

    @SerializedName("option_value_id")
    private String option_value_id;

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private String quantity;


    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getOption_value_id() {
        return option_value_id;
    }

    public String getProduct_option_value_id() {
        return product_option_value_id;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_excluding_tax() {
        return price_excluding_tax;
    }

    public String getPrice_formated() {
        return price_formated;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProduct_option_value_id(String product_option_value_id) {
        this.product_option_value_id = product_option_value_id;
    }
}
