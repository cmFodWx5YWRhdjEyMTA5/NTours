package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 8/6/17.
 */

public class ChangePasswordFields {


    @SerializedName("password")
    private String password;

    @SerializedName("confirm")
    private String confirm;

    public ChangePasswordFields(String password, String confirm) {

        this.password = password;
        this.confirm = confirm;


    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
    }
}
