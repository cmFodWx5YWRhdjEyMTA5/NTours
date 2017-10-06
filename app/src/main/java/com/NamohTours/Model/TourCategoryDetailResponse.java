package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 14/5/17.
 */

public class TourCategoryDetailResponse {



    /*"data": [
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
    },*/

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("description")
    private String description;

    @SerializedName("parent_id")
    private String parent_id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("original_image")
    private String original_image;

    @SerializedName("filters")
    private TourCategoryFilter filters;


    @SerializedName("categories")
    private List<TourCategoryDetailResponse> subCategoryResponse;


    public String getCategory_id() {
        return category_id;
    }

    public String getDescription() {
        return description;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_image() {
        return original_image;
    }

    public TourCategoryFilter getFilters() {
        return filters;
    }

    public List<TourCategoryDetailResponse> getSubCategoryResponse() {

        return subCategoryResponse != null ? subCategoryResponse : null;
    }

    public void setSubCategoryResponse(List<TourCategoryDetailResponse> subCategoryResponse) {
        this.subCategoryResponse = subCategoryResponse;
    }

    public int getCount() {
        return getSubCategoryResponse().size();
    }
}
