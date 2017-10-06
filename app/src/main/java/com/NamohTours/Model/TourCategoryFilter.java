package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 15/5/17.
 */

public class TourCategoryFilter {


    @SerializedName("filter_groups")
    private List<TourCategoryFilterGroups> filter_groups;

    public List<TourCategoryFilterGroups> getFilter_groups() {
        return filter_groups;
    }
}
