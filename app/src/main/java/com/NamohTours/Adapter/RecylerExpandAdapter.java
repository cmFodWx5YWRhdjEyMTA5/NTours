package com.NamohTours.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NamohTours.R;
import com.NamohTours.View.ChildHolder;
import com.NamohTours.View.ExtratabHeader;
import com.NamohTours.View.HeaderViewholder;
import com.NamohTours.View.ProductExtraTabHeader;
import com.NamohTours.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.NamohTours.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by ubuntu on 11/7/17.
 */

public class RecylerExpandAdapter extends ExpandableRecyclerViewAdapter<HeaderViewholder, ChildHolder> {


    private Context context;


    public RecylerExpandAdapter(Context context, List<? extends ExpandableGroup> list) {
        super(list);

        this.context = context;
    }

    @Override
    public HeaderViewholder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.extra_tab_list_group, parent, false);

        return new HeaderViewholder(view);
    }

    @Override
    public ChildHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.extra_tab_list_item, null, false);

        return new ChildHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final ProductExtraTabHeader header = ((ExtratabHeader) group).getItems().get(childIndex);

        holder.onBind(header, group);


    }

    @Override
    public void onBindGroupViewHolder(HeaderViewholder holder, int flatPosition, ExpandableGroup group) {

        holder.setGroupName(group);

    }


}
