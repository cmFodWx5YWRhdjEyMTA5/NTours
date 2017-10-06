package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 8/5/17.
 */

public class UserLoginFields {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public UserLoginFields(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
