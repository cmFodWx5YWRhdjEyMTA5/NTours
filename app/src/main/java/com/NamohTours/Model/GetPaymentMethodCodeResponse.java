package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 8/9/17.
 */

public class GetPaymentMethodCodeResponse {

    @SerializedName("code")
    private String code;


    @SerializedName("title")
    private String title;

    @SerializedName("terms")
    private String terms;

    @SerializedName("sort_order")
    private String sort_order;


    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getSort_order() {
        return sort_order;
    }

    public String getTerms() {
        return terms;
    }
}
