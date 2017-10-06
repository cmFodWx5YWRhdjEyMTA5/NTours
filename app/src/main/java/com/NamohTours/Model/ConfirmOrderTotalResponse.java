package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ConfirmOrderTotalResponse {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
