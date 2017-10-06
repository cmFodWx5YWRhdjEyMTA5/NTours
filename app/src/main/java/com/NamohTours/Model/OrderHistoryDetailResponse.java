package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class OrderHistoryDetailResponse {

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("total")
    private String total;

    @SerializedName("payment_method")
    private String payment_method;

    @SerializedName("order_status_id")
    private String order_status_id;


    @SerializedName("order_status")
    private String order_status;

    @SerializedName("date_modified")
    private String date_modified;


    @SerializedName("date_added")
    private String date_added;

    @SerializedName("products")
    private List<OrderHistoryProductResponse> products;

    @SerializedName("histories")
    private List<OrderHistoriesResponse> histories;


    @SerializedName("vouchers")
    private List<Object> vouchers;

    @SerializedName("totals")
    private List<OrderHistoryPriceResponse> totals;

    public String getOrder_id() {
        return order_id;
    }


    public String getTotal() {
        return total;
    }


    public String getOrder_status_id() {
        return order_status_id;
    }


    public String getOrder_status() {
        return order_status;
    }


    public String getDate_modified() {
        return date_modified;
    }


    public String getDate_added() {
        return date_added;
    }


    public List<OrderHistoryProductResponse> getProducts() {
        return products;
    }


    public List<OrderHistoriesResponse> getHistories() {
        return histories;
    }


    public List<Object> getVouchers() {
        return vouchers;
    }


    public List<OrderHistoryPriceResponse> getTotals() {
        return totals;
    }

    public String getPayment_method() {
        return payment_method;
    }
}
