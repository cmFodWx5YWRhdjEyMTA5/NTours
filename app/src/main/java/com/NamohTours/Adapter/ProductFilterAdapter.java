package com.NamohTours.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.NamohTours.Model.TourCategoryFilterGroups;
import com.NamohTours.Model.TourCategoryFilterGroupsDetails;
import com.NamohTours.R;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

/**
 * Created by ubuntu on 30/5/17.
 */

public class ProductFilterAdapter extends SectionedRecyclerViewAdapter<ProductFilterAdapter.FilterHeaderViewHolder, ProductFilterAdapter.FilterItemViewHolder, RecyclerView.ViewHolder> {

    private List<TourCategoryFilterGroups> FilterGroupList;
    private Context context;


    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public ProductFilterAdapter(List<TourCategoryFilterGroups> prod, Context context, @NonNull OnItemCheckListener onItemCheckListener) {
        this.FilterGroupList = prod;
        this.context = context;
        this.onItemCheckListener = onItemCheckListener;

    }


    @Override
    protected int getSectionCount() {
        return FilterGroupList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return FilterGroupList.get(section).getTourCategoryFilterGroupsDetailses().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected FilterHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_filter_header, parent, false);
        return new FilterHeaderViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected FilterItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_filter_items, parent, false);
        return new FilterItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(FilterHeaderViewHolder holder, int section) {

        holder.txtHeader.setText(FilterGroupList.get(section).getFilter_group_name());


    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }


    @Override
    protected void onBindItemViewHolder(final FilterItemViewHolder holder, int section, int position) {


        final TourCategoryFilterGroupsDetails details = FilterGroupList.get(section).getTourCategoryFilterGroupsDetailses().get(position);


        holder.chkFilter.setText(details.getFilter_name());
        holder.setOnClickListener(null);
        holder.chkFilter.setChecked(details.isSelected());
        holder.chkFilter.setTag(details.getFilter_id());


        ((FilterItemViewHolder) holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FilterItemViewHolder) holder).chkFilter.setChecked(!((FilterItemViewHolder) holder).chkFilter.isChecked());

                if (((FilterItemViewHolder) holder).chkFilter.isChecked()) {
                    onItemCheckListener.onItemCheck(details);
                    details.setSelected(true);
                } else {
                    onItemCheckListener.onItemUncheck(details);
                    details.setSelected(false);
                }
            }
        });
    }

       /* holder.chkFilter.setText(details.getFilter_name());
        holder.setOnClickListener(null);
        holder.chkFilter.setChecked(details.isSelected());
        holder.chkFilter.setTag(details.getFilter_id());



        ((FilterItemViewHolder)holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FilterItemViewHolder)holder).chkFilter.setChecked(!((FilterItemViewHolder)holder).chkFilter.isChecked());

                if(((FilterItemViewHolder)holder).chkFilter.isChecked())
                {
                    onItemCheckListener.onItemCheck(details);
                    details.setSelected(true);
                }
                else
                {
                  onItemCheckListener.onItemUncheck(details);
                    details.setSelected(false);
                }
            }
        });*/





       /* final TourCategoryFilterGroupsDetails details= pros.get(section).getTourCategoryFilterGroupsDetailses().get(position);


        holder.chkFilter.setText(details.getFilter_name());
        holder.setOnClickListener(null);
        holder.chkFilter.setChecked(details.isSelected());
        holder.chkFilter.setTag(details.getFilter_id());

        ((FilterStickyAdapter.CategoryViewHolder)holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FilterStickyAdapter.CategoryViewHolder)holder).chkFilter.setChecked(!((FilterStickyAdapter.CategoryViewHolder)holder).chkFilter.isChecked());

                if (((FilterStickyAdapter.CategoryViewHolder) holder).chkFilter.isChecked())
                {
                    onItemCheckListener.onItemCheck(details);
                    details.setSelected(true);

                } else
                {
                    onItemCheckListener.onItemUncheck(details);
                    details.setSelected(false);
                }
            }
        });
*/


    public static class FilterHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView txtHeader;

        public FilterHeaderViewHolder(View view) {
            super(view);

            txtHeader = (TextView) view.findViewById(R.id.tour_filter_heading);
        }
    }


    public static class FilterItemViewHolder extends RecyclerView.ViewHolder {

        CheckBox chkFilter;

        View itemView;

        public FilterItemViewHolder(View v) {
            super(v);
            this.itemView = v;

            chkFilter = (CheckBox) v.findViewById(R.id.chkFilter);
            chkFilter.setClickable(false);
          /*  data = (TextView) v.findViewById(R.id.txtFilterTitle);
           chkFilter =(CheckBox)v.findViewById(R.id.chkFilter);*/

        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }


    public interface OnItemCheckListener {
        void onItemCheck(TourCategoryFilterGroupsDetails item);

        void onItemUncheck(TourCategoryFilterGroupsDetails item);
    }

}
