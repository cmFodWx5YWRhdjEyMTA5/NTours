package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class ProductGetCartDetailResponse {

    @SerializedName("error_warning")
    private String error_warning;

    @SerializedName("attention")
    private String attention;

    @SerializedName("weight")
    private String weight;


    @SerializedName("products")
    private List<ProductGetCartProductsList> cartProductsList;

    @SerializedName("total")
    private String total;

    @SerializedName("total_raw")
    private String total_raw;

    @SerializedName("total_product_count")
    private String total_product_count;


    @SerializedName("coupon_status")
    private String coupon_status;

    @SerializedName("coupon")
    private String coupon;

    @SerializedName("voucher_status")
    private String voucher_status;

    @SerializedName("voucher")
    private String voucher;

    @SerializedName("reward_status")
    private String reward_status;

    @SerializedName("reward")
    private String reward;

    @SerializedName("totals")
    private List<ProductGetCartTotalPriceResponse> priceResponse;


    public String getError_warning() {
        return error_warning;
    }

    public List<ProductGetCartProductsList> getCartProductsList() {
        return cartProductsList;
    }


    public String getCoupon() {
        return coupon;
    }

    public String getTotal() {
        return total;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public String getReward() {
        return reward;
    }

    public String getTotal_raw() {
        return total_raw;
    }

    public String getTotal_product_count() {
        return total_product_count;
    }

    public String getReward_status() {
        return reward_status;
    }

    public String getVoucher() {
        return voucher;
    }

    public String getVoucher_status() {
        return voucher_status;
    }

    public List<ProductGetCartTotalPriceResponse> getPriceResponse() {
        return priceResponse;
    }
}
