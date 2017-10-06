package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 10/5/17.
 */

public class UserAccountResponse {

    @SerializedName("success")
    String success;

    @SerializedName("error")
    String error;

    @SerializedName("data")
    UserAccountDetailResponse data;

    public String getSuccess() {
        return success;
    }

    public UserAccountDetailResponse getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
