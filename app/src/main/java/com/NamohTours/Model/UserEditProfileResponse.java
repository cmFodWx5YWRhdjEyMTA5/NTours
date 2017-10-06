package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 11/6/17.
 */

public class UserEditProfileResponse {


    @SerializedName("success")
    private Boolean success;

    @SerializedName("error")
    private UserEditProfileResponse warning;


    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("email")
    private String email;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("telephone")
    private String telephone;


    public UserEditProfileResponse(String email, String firstname, String lastname, String telephone) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;

    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setWarning(UserEditProfileResponse warning) {
        this.warning = warning;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public Boolean getSuccess() {
        return success;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public UserEditProfileResponse getWarning() {
        return warning;
    }

    public String getError_description() {
        return error_description;
    }
}
