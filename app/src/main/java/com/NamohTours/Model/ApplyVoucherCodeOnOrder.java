package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ApplyVoucherCodeOnOrder {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("voucher")
    private String voucher;


    public ApplyVoucherCodeOnOrder(String voucher) {
        this.voucher = voucher;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
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

    public String getError() {
        return error;
    }

    public String getVoucher() {
        return voucher;
    }
}
