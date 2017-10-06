package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 4/9/17.
 */

public class UpdateCartProductQuantity {
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

    @SerializedName("key")
    private String key;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("type")
    private String type;


    public UpdateCartProductQuantity(String key, String quantity, String type) {
        this.key = key;
        this.quantity = quantity;
        this.type = type;
    }


    public String getSuccess() {
        return success;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getError_description() {
        return error_description;
    }

    public String getKey() {
        return key;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public String getWarning() {
        return warning;
    }
}
