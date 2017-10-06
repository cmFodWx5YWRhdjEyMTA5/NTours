package com.NamohTours.Adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.NamohTours.Interface.PicassoCache;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.ComapreProductDetailResponse;
import com.NamohTours.Model.GetWishlistProductDetailResponse;
import com.NamohTours.Model.GetWishlistProductsListResponse;
import com.NamohTours.Model.TourProductDetailResponse;
import com.NamohTours.R;

import java.util.List;

/**
 * Created by Pooja Mantri on 28/7/17.
 */

public class WishlistProductAdapter extends RecyclerView.Adapter<WishlistProductAdapter.WishListHolder> {

    private List<ComapreProductDetailResponse> products;
    private int rowLayout;
    private Context context;
    private ClickListener ItemListener;

    public DBManager dbManager;
    private WishListClick WishclickListner;

    public static class WishListHolder extends RecyclerView.ViewHolder {

        LinearLayout productsLayout;

        TextView productTitle;
        TextView data;
        TextView productSpecialPrice;
        ImageView productImage;
        ImageView productWish;

        public WishListHolder(View v) {
            super(v);

            productsLayout = (LinearLayout) v.findViewById(R.id.tour_Wishlistproducts_layout);

            productTitle = (TextView) v.findViewById(R.id.tour_Wishlistproduct_title);
            data = (TextView) v.findViewById(R.id.tour_Wishlistproduct_subtitle);
            productSpecialPrice = (TextView) v.findViewById(R.id.tour_Wishlistproduct_special);
            productImage = (ImageView) v.findViewById(R.id.tour_Wishlistimage_product);
            productWish = (ImageView) v.findViewById(R.id.WishproductWish);

        }
    }

    public WishlistProductAdapter(List<ComapreProductDetailResponse> products, int rowLayout, Context context, ClickListener itemListener, WishListClick wishclickListner) {
        this.products = products;
        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = itemListener;
        this.WishclickListner = wishclickListner;
    }

    @Override
    public WishListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new WishlistProductAdapter.WishListHolder(view);
    }

    @Override
    public void onBindViewHolder(WishListHolder holder, int position) {


        // if special price available
        if (!(products.get(position).getSpecial().equals("false"))) {


            holder.productSpecialPrice.setVisibility(View.VISIBLE);

            holder.productSpecialPrice.setText("\u0020" + products.get(position).getSpecial());

            holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
            holder.data.setText("\u0020" + products.get(position).getPrice());
            holder.data.setPaintFlags(holder.data.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            PicassoCache.getPicassoInstance(context).load(products.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);


        }

        // else show default product price

        else {
            // if category id is null
            if (!(products.get(position).getPrice().startsWith("0"))) {

                //  \u20B9 for Rupess symbol
                //  \u0020 for space
                holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
                holder.data.setText("\u0020" + products.get(position).getPrice());

                PicassoCache.getPicassoInstance(context).load(products.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);

            } else {

                holder.productTitle.setText(Html.fromHtml(products.get(position).getName()));
                holder.data.setText("Contact for Price");

                PicassoCache.getPicassoInstance(context).load(products.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);


            }

        }


        applyClickEvents(holder, position);

    }

    private void applyClickEvents(final WishlistProductAdapter.WishListHolder holder, int position) {

        final int pos = position;
        holder.productsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemListener.onClick(pos);
            }
        });

        holder.productWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WishclickListner.onClick(pos);
                WishPro(holder, pos, v);

            }
        });

    }


    public void WishPro(WishlistProductAdapter.WishListHolder holder, int position, View v) {
        dbManager = new DBManager(context);
        dbManager.open();


        boolean checkWish = dbManager.checkAlreadyExist(products.get(holder.getAdapterPosition()).getProduct_id());

        if (checkWish) {

            products.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), products.size());
            //   notifyDataSetChanged();
            //notifyItemChanged(position,products.size());
            //  notifyDataSetChanged();

        }

    }


    @Override
    public int getItemCount() {
        return products.size();
    }


}
