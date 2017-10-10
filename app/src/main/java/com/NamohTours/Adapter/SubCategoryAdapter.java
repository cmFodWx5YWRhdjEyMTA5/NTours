package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.PicassoCache;
import com.NamohTours.Model.TourCategoryDetailResponse;
import com.NamohTours.R;

import java.util.List;

/**
 * Created by ubuntu on 24/5/17.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder> {

    private List<TourCategoryDetailResponse> products;
    private int rowLayout;
    private Context context;
    private ClickListener ItemListener;


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productsLayout;

        TextView productTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView productImage;


        public CategoryViewHolder(View v) {
            super(v);
            productsLayout = (LinearLayout) v.findViewById(R.id.subCategory_layout);

            productTitle = (TextView) v.findViewById(R.id.subCategory_title);
            data = (TextView) v.findViewById(R.id.subCategory_subtitle);
            // movieDescription = (TextView) v.findViewById(R.id.description);
            productImage = (ImageView) v.findViewById(R.id.subCategory_image);


        }
    }

    public SubCategoryAdapter(List<TourCategoryDetailResponse> products, int rowLayout, Context context, ClickListener listener) {
        this.products = products;
        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = listener;

    }

    @Override
    public SubCategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SubCategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.CategoryViewHolder holder, int position) {


        if (products.get(position).getSubCategoryResponse() != null) {

            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText(String.valueOf(products.get(position).getCount()) + "Categories");

            // Picasso.with(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

            PicassoCache.getPicassoInstance(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);
        } else {

            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText("");

            PicassoCache.getPicassoInstance(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

            // Picasso.with(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

        }

        applyClickEvents(holder, position);

    }

    private void applyClickEvents(CategoryViewHolder holder, final int position) {
        holder.productsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListener.onClick(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return products.size();
    }


}

