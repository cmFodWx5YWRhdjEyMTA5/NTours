package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by ubuntu on 4/5/17.
 */

public class UserLoginRegisterDetailResponse {


    @SerializedName("firstname")
    private String FirstName;

    @SerializedName("lastname")
    private String LastName;

    @SerializedName("city")
    private String City;

    @SerializedName("email")
    private String Email;

    @SerializedName("country_id")
    private String country_id;

    @SerializedName("address_1")
    private String address_1;


    @SerializedName("telephone")
    private String telephone;

    @SerializedName("zone_id")
    private String zone_id;

    @SerializedName("password")
    private String password;

    @SerializedName("confirm")
    private String confirm;


    @SerializedName("agree")
    private Integer agree;


    @SerializedName("success")
    private String success;


    // UserLoginRegisterResponse is a bean class
    @SerializedName("warning")
    private String error;

    @SerializedName("country")
    private String country;


    @SerializedName("zone")
    private String zone;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("data")
    private JSONObject data;


    public UserLoginRegisterDetailResponse(String address1, String city, String country_id, String email, String firstname, String lastname, String telephone,
                                           String zoneid, String password, String confirm, Integer agrre) {
        this.address_1 = address1;
        this.City = city;
        this.country_id = country_id;
        this.Email = email;
        this.FirstName = firstname;
        this.LastName = lastname;
        this.telephone = telephone;
        this.zone_id = zoneid;
        this.password = password;
        this.confirm = confirm;
        this.agree = agrre;

    }


    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getCity() {
        return City;

    }

    public String getAddress_1() {
        return address_1;
    }

    public Integer getAgree() {
        return agree;
    }

    public String getCountry() {
        return country;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getZone() {
        return zone;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public void setAgree(Integer agree) {
        this.agree = agree;
    }


    public String getError() {
        return error;
    }

    public String getSuccess() {
        return success;
    }

    public JSONObject getData() {
        return data;
    }


}



