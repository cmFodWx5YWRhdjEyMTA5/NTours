package com.NamohTours.Model;

import com.NamohTours.Activity.SearchResults;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Pooja Mantri on 8/9/17.
 */

public class GetPaymentMethodDetailResponse {

    @SerializedName("error_warning")
    private String error_warning;

    @SerializedName("payment_methods")
    private HashMap<String, GetPaymentMethodCodeResponse> payment_methods;

    @SerializedName("code")
    private String code;

    @SerializedName("comment")
    private String comment;

    @SerializedName("agree")
    private String agree;


    public String getError_warning() {
        return error_warning;
    }

    public HashMap<String, GetPaymentMethodCodeResponse> getPayment_methods() {
        return payment_methods;
    }

    public String getCode() {
        return code;
    }

    public String getAgree() {
        return agree;
    }

    public String getComment() {
        return comment;
    }

}
