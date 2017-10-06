package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 10/5/17.
 */

public class UserAccountDetailResponse {

    @SerializedName("customer_id")
    String customer_id;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("email")
    String email;

    @SerializedName("reward_total")
    String reward_total;

    @SerializedName("telephone")
    String telephone;

    @SerializedName("cart")
    String cart;

    @SerializedName("wishlist")
    String wishlist;


    @SerializedName("rewards")
    private List<UserAccountRewardsDetails> rewardsDetailsList;


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getReward_total() {
        return reward_total;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public List<UserAccountRewardsDetails> getRewardsDetailsList() {
        return rewardsDetailsList;
    }
}
