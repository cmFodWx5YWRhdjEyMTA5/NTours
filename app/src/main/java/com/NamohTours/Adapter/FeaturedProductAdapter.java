package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Database.DBManager;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.TourFeaturedProductsListResponse;
import com.NamohTours.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ubuntu on 22/6/17.
 */

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ProductViewHolder> {

    public DBManager dbManager;
    private List<TourFeaturedProductsListResponse> products;
    private int rowLayout;
    private Context context;
    private ClickListener ItemListener;
    private WishListClick WishclickListner;

    public FeaturedProductAdapter(List<TourFeaturedProductsListResponse> products, int rowLayout, Context context, ClickListener listener) {
        this.products = products;
        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = listener;

    }

    @Override
    public FeaturedProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FeaturedProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {


        if (!(products.get(position).getPrice().startsWith("0"))) {

            //  \u20B9 for Rupess symbol
            //  \u0020 for space
            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText("\u20B9" + "\u0020" + products.get(position).getPrice());
            //holder.movieDescription.setText(products.get(position).getModel());
            Picasso.with(context).load(products.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

            //Picasso.with(context).load(products.get(position).getImage())

            getWish(holder, position);
        } else {

            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText("Contact for Price");
            //holder.movieDescription.setText(products.get(position).getModel());
            Picasso.with(context).load(products.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

            //Picasso.with(context).load(products.get(position).getImage())
            getWish(holder, position);
        }


        applyClickEvents(holder, position);

    }

    private void applyClickEvents(final ProductViewHolder holder, final int position) {
        holder.productsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListener.onClick(position);
            }
        });

        holder.productWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishPro(holder, position, v);
            }
        });


    }

    public void getWish(final FeaturedProductAdapter.ProductViewHolder holder, final int position) {
        dbManager = new DBManager(context);
        dbManager.open();

        boolean getWishs = dbManager.checkWishListExist(products.get(position).getProduct_id());

        if (getWishs) {
            holder.productWish.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_wish));

        } else {
            holder.productWish.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_no_wish));

        }


    }

    public void WishPro(final FeaturedProductAdapter.ProductViewHolder holder, final int position, View v) {
        dbManager = new DBManager(context);
        dbManager.open();

        boolean checkWish = dbManager.checkAlreadyExist(products.get(position).getProduct_id());

        if (checkWish) {

            holder.productWish.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_no_wish));
            // notifyDataSetChanged();
        } else {


            holder.productWish.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_wish));

            // notifyDataSetChanged();
        }

        notifyItemChanged(position);

        Log.e("Wish", "List" + dbManager.getWish());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productsLayout;

        TextView productTitle;
        TextView data;
        ImageView productImage;
        ImageView productWish;


        public ProductViewHolder(View v) {
            super(v);
            productsLayout = (LinearLayout) v.findViewById(R.id.tour_products_layout);

            productTitle = (TextView) v.findViewById(R.id.tour_product_title);
            data = (TextView) v.findViewById(R.id.tour_product_subtitle);
            //  movieDescription = (TextView) v.findViewById(R.id.tour_product_description);
            productImage = (ImageView) v.findViewById(R.id.tour_image_product);
            productWish = (ImageView) v.findViewById(R.id.productWish);

        }
    }
}
