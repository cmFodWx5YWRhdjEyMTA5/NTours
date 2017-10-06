package com.NamohTours.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Activity.TourProduct;
import com.NamohTours.Database.DBManager;
import com.NamohTours.Database.DatabaseHelper;
import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.PicassoCache;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.R;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<TourProductDetailResponse> products;
    private int rowLayout;
    private Context context;
    private ClickListener ItemListener;
    private WishListClick WishclickListner;

    public DBManager dbManager;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productsLayout;

        TextView productTitle;
        TextView data;
        TextView productSpecialPrice;
        ImageView productImage;
        ImageView productWish;


        public ProductViewHolder(View v) {
            super(v);
            productsLayout = (LinearLayout) v.findViewById(R.id.tour_products_layout);
            productTitle = (TextView) v.findViewById(R.id.tour_product_title);
            data = (TextView) v.findViewById(R.id.tour_product_subtitle);
            productSpecialPrice = (TextView) v.findViewById(R.id.tour_product_special);
            productImage = (ImageView) v.findViewById(R.id.tour_image_product);
            productWish = (ImageView) v.findViewById(R.id.productWish);

        }
    }

    public ProductAdapter(List<TourProductDetailResponse> products, int rowLayout, Context context, ClickListener itemListener, WishListClick wishclickListner) {
        this.products = products;

        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = itemListener;
        this.WishclickListner = wishclickListner;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {


        // if special price available
        if (!(products.get(position).getSpecial().equals("false"))) {


            holder.productSpecialPrice.setVisibility(View.VISIBLE);

            holder.productSpecialPrice.setText("\u20B9" + "\u0020" + products.get(position).getSpecial());

            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText("\u20B9" + "\u0020" + products.get(position).getPrice());
            holder.data.setPaintFlags(holder.data.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            PicassoCache.getPicassoInstance(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);


            // WishPro(holder,position);
            getWish(holder, position);


        }

        // else show default product price

        else {
            // if category id is null
            if (!(products.get(position).getPrice().startsWith("0"))) {

                //  \u20B9 for Rupess symbol
                //  \u0020 for space
                holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
                holder.data.setText("\u20B9" + "\u0020" + products.get(position).getPrice());

                PicassoCache.getPicassoInstance(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

                // WishPro(holder,position);

                getWish(holder, position);

            } else {

                holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
                holder.data.setText("Contact for Price");

                PicassoCache.getPicassoInstance(context).load(products.get(position).getOriginal_image()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

                //   WishPro(holder,position);

                getWish(holder, position);


            }

        }


        applyClickEvents(holder, position);

    }

    private void applyClickEvents(final ProductAdapter.ProductViewHolder holder, final int position) {
        holder.productsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListener.onClick(position);
            }
        });

        holder.productWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WishclickListner.onClick(position);
                WishPro(holder, position, v);


            }
        });


    }


    public void getWish(final ProductAdapter.ProductViewHolder holder, final int position) {
        dbManager = new DBManager(context);
        dbManager.open();

        boolean getWishs = dbManager.checkWishListExist(products.get(position).getId());

        if (getWishs) {
            holder.productWish.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_mwish));

        } else {
            holder.productWish.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_no_wish));

        }


        dbManager.close();


    }

    public void WishPro(final ProductAdapter.ProductViewHolder holder, final int position, View v) {
        dbManager = new DBManager(context);
        dbManager.open();

        boolean checkWish = dbManager.checkAlreadyExist(products.get(position).getId());

        if (checkWish) {

            holder.productWish.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_no_wish));
            // notifyDataSetChanged();
        } else {

            holder.productWish.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_mwish));

            // notifyDataSetChanged();
        }

        notifyItemChanged(position);

        dbManager.close();


    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}

