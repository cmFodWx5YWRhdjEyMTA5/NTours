package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class GetWishlistProductDetailResponse {
    @SerializedName("products")
    List<GetWishlistProductsListResponse> wishListProductList;

    public List<GetWishlistProductsListResponse> getWishListProductList() {
        return wishListProductList;
    }

    public void setWishListProductList(List<GetWishlistProductsListResponse> wishListProductList) {
        this.wishListProductList = wishListProductList;
    }
}
