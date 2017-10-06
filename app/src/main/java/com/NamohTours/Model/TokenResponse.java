package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 4/5/17.
 */

public class TokenResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;

    @SerializedName("access_token")
    private String Token;

    @SerializedName("expires_in")
    private String Expires;

    @SerializedName("token_type")
    private String TokenType;

    @SerializedName("old_token")
    private String old_token;


    public TokenResponse(String old_token) {
        this.old_token = old_token;
    }


    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public String getToken() {
        return TokenType + " " + Token;
    }

    public String getTokenType() {
        return TokenType;
    }

    public String getExpires() {
        return Expires;
    }


    public void setOld_token(String old_token) {
        this.old_token = old_token;
    }
}
