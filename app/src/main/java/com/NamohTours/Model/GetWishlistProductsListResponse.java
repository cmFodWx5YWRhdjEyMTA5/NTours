package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class GetWishlistProductsListResponse {


    /*{
                "product_id": "66",
                "thumb": "http://namohtours.com/image/cache/catalog/Ncategory Images/Kashmir-500x500.jpg",
                "name": "Memorable Andaman 2N/3D",
                "model": "N10011",
                "stock": "In Stock",
                "price": "₹6,900",
                "special": "₹4,900"
            }*/

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("stock")
    private String stock;

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

    public String getStock() {
        return stock;
    }


}

