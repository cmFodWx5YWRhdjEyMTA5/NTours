package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 3/5/17.
 */

public class Product {

    @SerializedName("name")
    private String name;
    @SerializedName("pirce")
    private String price;
    @SerializedName("id")
    private Integer id;
    @SerializedName("description")
    private String description;
    @SerializedName("rating")
    private Double rating;

    public Product(String name, String price, String description, Integer id, Double rating) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
        this.rating = rating;


    }


    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String title) {
        this.price = price;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
