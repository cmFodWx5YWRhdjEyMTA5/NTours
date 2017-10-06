package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ConfirmOrderDetailResponse {

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("products")
    private List<ConfirmOrderProductResponse> products;

  /*  @SerializedName("vouchers")
    private List<D> vouchers;*/

    @SerializedName("totals")
    private List<ConfirmOrderTotalResponse> totals;

    @SerializedName("payment")
    private String payment;


    public List<ConfirmOrderProductResponse> getProducts() {
        return products;
    }

    public List<ConfirmOrderTotalResponse> getTotals() {
        return totals;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPayment() {
        return payment;
    }


}
