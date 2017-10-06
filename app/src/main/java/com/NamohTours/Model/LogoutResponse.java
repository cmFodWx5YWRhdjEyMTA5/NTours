package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 9/5/17.
 */

public class LogoutResponse {

    @SerializedName("success")
    boolean success;

    public boolean getSuccess() {
        return success;
    }
}
