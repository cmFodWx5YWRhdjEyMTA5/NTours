package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Pooja Mantri on 29/7/17.
 */

public class CompareWishListProductResponse {

    private Map<String, ComapreProductDetailResponse> compareProduct;

    public Map<String, ComapreProductDetailResponse> getCompareProduct() {
        return compareProduct;
    }
}
