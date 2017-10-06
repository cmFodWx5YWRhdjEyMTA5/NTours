package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ConfirmOrderProductResponse {
    @SerializedName("key")
    private String key;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("option")
    private List<ProductGetCartOptionsDetails> optionsDetailsList;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("price")
    private String price;

    @SerializedName("total")
    private String total;

    @SerializedName("href")
    private String href;


    public String getKey() {
        return key;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public List<ProductGetCartOptionsDetails> getOptionsDetailsList() {
        return optionsDetailsList;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public String getHref() {
        return href;
    }
}
