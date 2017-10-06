package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class DeleteCartProductResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("error")
    private String warning;

    @SerializedName("total")
    private String total;

    @SerializedName("total_product_count")
    private String total_product_count;

    @SerializedName("total_price")
    private String total_price;

    @SerializedName("key")
    private String key;

    @SerializedName("type")
    private String type;


    public DeleteCartProductResponse(String key, String type) {
        this.key = key;
        this.type = type;

    }


    public String getSuccess() {
        return success;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getError_description() {
        return error_description;
    }

    public String getTotal() {
        return total;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getTotal_product_count() {
        return total_product_count;
    }

    public String getWarning() {
        return warning;
    }
}
