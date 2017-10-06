package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class ProductGetCartProductsList {

    @SerializedName("key")
    private String key;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("name")
    private String name;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("model")
    private String model;

    @SerializedName("option")
    private List<ProductGetCartOptionsDetails> optionsDetails;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("stock")
    private String stock;

    @SerializedName("reward")
    private String reward;

    @SerializedName("price")
    private String price;

    @SerializedName("total")
    private String total;


    public String getKey() {
        return key;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public List<ProductGetCartOptionsDetails> getOptionsDetails() {
        return optionsDetails;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getReward() {
        return reward;
    }

    public String getThumb() {
        return thumb;
    }

    public String getStock() {
        return stock;
    }

    public String getTotal() {
        return total;
    }


}

