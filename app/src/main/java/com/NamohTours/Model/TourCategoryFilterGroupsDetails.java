package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 17/5/17.
 */

public class TourCategoryFilterGroupsDetails {

    @SerializedName("filter_id")
    private String filter_id;

    @SerializedName("name")
    private String filter_name;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFilter_id() {
        return filter_id;
    }

    public String getFilter_name() {
        return filter_name;
    }
}
