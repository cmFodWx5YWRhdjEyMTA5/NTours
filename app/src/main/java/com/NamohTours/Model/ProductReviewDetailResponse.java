package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pooja Mantri on 27/7/17.
 */

public class ProductReviewDetailResponse {

    @SerializedName("review_total")
    private String review_total;

    @SerializedName("reviews")
    private List<ProductReviewDetailListResponse> reviews;

    public String getReview_total() {
        return review_total;
    }

    public List<ProductReviewDetailListResponse> getReviews() {
        return reviews;
    }

    public void setReview_total(String review_total) {
        this.review_total = review_total;
    }

    public void setReviews(List<ProductReviewDetailListResponse> reviews) {
        this.reviews = reviews;
    }

}
