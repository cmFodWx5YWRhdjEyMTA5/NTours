package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 27/7/17.
 */

public class ProductReviewDetailListResponse {

    /*"author": "Pooja",
          "text": "Andaman is my favorite Holiday Destination",
          "rating": 4,
          "date_added": "26/07/2017"*/

    @SerializedName("author")
    private String author;

    @SerializedName("text")
    private String text;

    @SerializedName("rating")
    private String rating;

    @SerializedName("date_added")
    private String date_added;

    // getter Methods

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getRating() {
        return rating;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }


}
