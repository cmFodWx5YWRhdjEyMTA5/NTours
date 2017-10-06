package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ubuntu on 17/5/17.
 */

public class TourCategoryFilterGroups {

    @SerializedName("filter_group_id")
    private String filter_group_id;

    @SerializedName("name")
    private String filter_group_name;

    @SerializedName("filter")
    private List<TourCategoryFilterGroupsDetails> tourCategoryFilterGroupsDetailses;

    public String getFilter_group_id() {
        return filter_group_id;
    }

    public String getFilter_group_name() {
        return filter_group_name;
    }

    public List<TourCategoryFilterGroupsDetails> getTourCategoryFilterGroupsDetailses() {
        return tourCategoryFilterGroupsDetailses;
    }
}
