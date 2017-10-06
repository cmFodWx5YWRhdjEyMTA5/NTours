package com.NamohTours.View;

import android.annotation.SuppressLint;

import com.NamohTours.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by ubuntu on 11/7/17.
 */

@SuppressLint("ParcelCreator")

public class ExtratabHeader extends ExpandableGroup<ProductExtraTabHeader> {


    public ExtratabHeader(String title, List<ProductExtraTabHeader> items) {
        super(title, items);
    }
}
