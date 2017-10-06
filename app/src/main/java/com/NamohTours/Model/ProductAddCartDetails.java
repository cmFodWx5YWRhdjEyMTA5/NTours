package com.NamohTours.Model;

import android.widget.LinearLayout;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pooja Mantri on 29/8/17.
 */

public class ProductAddCartDetails {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("option")
    private HashMap<String, Object> option;

    @SerializedName("type")
    private String type;

    public ProductAddCartDetails(String product_id, String quantity, HashMap<String, Object> stringHashMap, String type) {

        this.product_id = product_id;
        this.quantity = quantity;
        this.option = stringHashMap;
        this.type = type;

    }


    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setOption(HashMap<String, Object> option) {
        this.option = option;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getQuantity() {
        return quantity;
    }


    public HashMap<String, Object> getOption() {
        return option;
    }
}
