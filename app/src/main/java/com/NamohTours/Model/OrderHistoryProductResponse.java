package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class OrderHistoryProductResponse {

    @SerializedName("order_product_id")
    private String order_product_id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;


    @SerializedName("sku")
    private String sku;

    @SerializedName("option")
    private List<OrderHistoryOptionResponse> option;


    @SerializedName("quantity")
    private String quantity;


    @SerializedName("price_excluding_tax")
    private String price_excluding_tax;


    @SerializedName("price")
    private String price;


    @SerializedName("total")
    private String total;

    public String getOrder_product_id() {
        return order_product_id;
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


    public String getSku() {
        return sku;
    }


    public List<OrderHistoryOptionResponse> getOption() {
        return option;
    }


    public String getQuantity() {
        return quantity;
    }


    public String getPrice_excluding_tax() {
        return price_excluding_tax;
    }


    public String getPrice() {
        return price;
    }


    public String getTotal() {
        return total;
    }


}
