package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 14/5/17.
 */

public class TourCategoryResponse {


    // if Token is valid

    /*
    * "success": true,
  "data": [
    {
      "category_id": "20",
      "parent_id": "0",
      "name": "Desktops",
      "image": "http://opencartoauth.opencart-api.com/image/cache/catalog/demo/compaq_presario-500x500.jpg",
      "original_image": "http://opencartoauth.opencart-api.com/image/catalog/demo/compaq_presario.jpg",
      "filters": {
        "filter_groups": []
      },
      "categories": null
    },.......... */


    // if token is invalid

    /*
    "statusCode": 401,
  "statusText": "Unauthorized",
  "error_description": "The access token provided is invalid"*/


    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private List<TourCategoryDetailResponse> tourCategoryDetailResponses;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    @SerializedName("error")
    private String error;

    public String getSuccess() {
        return success;
    }

    public List<TourCategoryDetailResponse> getTourCategoryDetailResponses() {
        return tourCategoryDetailResponses;
    }

    public String getError_description() {
        return error_description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getError() {
        return error;
    }
}
