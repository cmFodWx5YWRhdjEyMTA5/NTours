package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 7/9/17.
 */

public class GetUserPaymentAddressDetailResponse {


    @SerializedName("address_id")
    private String address_id;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("address_1")
    private String address_1;

    @SerializedName("city")
    private String city;

    @SerializedName("zone")
    private String zone;

    @SerializedName("country")
    private String country;


    public String getAddress_id() {
        return address_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress_1() {
        return address_1;
    }

    public String getCity() {
        return city;
    }

    public String getZone() {
        return zone;
    }

    public String getCountry() {
        return country;
    }
}
