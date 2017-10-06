package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 7/9/17.
 */

public class GetUserPaymentAddressResponse {

    @SerializedName("address_id")
    private String address_id;

    @SerializedName("addresses")
    private List<GetUserPaymentAddressDetailResponse> addressList;

    @SerializedName("country_id")
    private String country_id;

    @SerializedName("zone_id")
    private String zone_id;


    public List<GetUserPaymentAddressDetailResponse> getAddressList() {
        return addressList;
    }

    public String getAddress_id() {
        return address_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getZone_id() {
        return zone_id;
    }


}
