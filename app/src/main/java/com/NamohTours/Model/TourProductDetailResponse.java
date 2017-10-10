package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 9/5/17.
 */

public class TourProductDetailResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("model")
    private String model;

    @SerializedName("image")
    private String image;

    @SerializedName("images")
    private ArrayList<String> imagesList;

    @SerializedName("price")
    private String price;

    @SerializedName("special")
    private String special;

    @SerializedName("description")
    String description;

    @SerializedName("original_image")
    private String original_image;

    @SerializedName("extratabbs")
    private List<TourProductextraTabResponse> extraTabResponse;


    @SerializedName("original_images")
    private ArrayList<String> original_imagesList;

    @SerializedName("options")
    private List<TourProductOptionsResponse> tourProductOptionsResponses;

    @SerializedName("reviews")
    private ProductReviewDetailResponse response;

    @SerializedName("quantity")
    private String quantity;


    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecial() {
        return special;
    }

    public List<TourProductOptionsResponse> getTourProductOptionsResponses() {
        return tourProductOptionsResponses;
    }

    public List<TourProductextraTabResponse> getExtraTabResponse() {
        return extraTabResponse;
    }

    public ProductReviewDetailResponse getResponse() {
        return response;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public ArrayList<String> getOriginal_imagesList() {
        return original_imagesList;
    }

    public String getOriginal_image() {
        return original_image;
    }


    public String getQuantity() {
        return quantity;
    }
}
