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

    public void setResponse(ProductReviewDetailResponse response) {
        this.response = response;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    public void setOriginal_imagesList(ArrayList<String> original_imagesList) {
        this.original_imagesList = original_imagesList;
    }

    public void setOriginal_image(String original_image) {
        this.original_image = original_image;
    }

    public String getOriginal_image() {
        return original_image;
    }

    public void setExtraTabResponse(List<TourProductextraTabResponse> extraTabResponse) {
        this.extraTabResponse = extraTabResponse;
    }
}
