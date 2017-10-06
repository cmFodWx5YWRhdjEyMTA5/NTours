package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.AddCartQtyClickListner;
import com.NamohTours.Interface.PicassoCache;
import com.NamohTours.Interface.SubtractCartQtyClickListner;
import com.NamohTours.Model.ProductGetCartProductsList;
import com.NamohTours.R;

import java.util.List;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {


    private Context context;
    private List<ProductGetCartProductsList> cartProductsList;
    private int rowLayout;
    private ClickListener ItemListener;
    private AddCartQtyClickListner addCartQtyClickListner;
    private SubtractCartQtyClickListner subtractCartQtyClickListner;

    public static class CartProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, productDelete, productQtyAdd, productSubtractQty;
        TextView productTitle, productQty, productPrice, productTotalPrice;


        public CartProductViewHolder(View itemView) {
            super(itemView);


            productImage = (ImageView) itemView.findViewById(R.id.cartProductImage);
            productTitle = (TextView) itemView.findViewById(R.id.cartProductTitle);
            productPrice = (TextView) itemView.findViewById(R.id.cartProductPriceTitle);
            productQty = (TextView) itemView.findViewById(R.id.cartProductQty);
            productDelete = (ImageView) itemView.findViewById(R.id.cartDelete);
            productQtyAdd = (ImageView) itemView.findViewById(R.id.addQtyImage);
            productSubtractQty = (ImageView) itemView.findViewById(R.id.subtractQtyImage);
            productTotalPrice = (TextView) itemView.findViewById(R.id.cartProductTotalPrice);


        }
    }


    public CartProductAdapter(List<ProductGetCartProductsList> productsList, int rowLayout, Context context, ClickListener itemListener, AddCartQtyClickListner addCartQtyClickListner, SubtractCartQtyClickListner subtractCartQtyClickListner) {
        this.cartProductsList = productsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = itemListener;
        this.addCartQtyClickListner = addCartQtyClickListner;
        this.subtractCartQtyClickListner = subtractCartQtyClickListner;
    }


    @Override
    public CartProductAdapter.CartProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CartProductAdapter.CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartProductAdapter.CartProductViewHolder holder, int position) {


        PicassoCache.getPicassoInstance(context).load(cartProductsList.get(position).getThumb()).placeholder(R.drawable.category_placeholder).into(holder.productImage);
        holder.productTitle.setText(cartProductsList.get(position).getName());
        holder.productQty.setText(cartProductsList.get(position).getQuantity());
        holder.productPrice.setText("Price : \t" + cartProductsList.get(position).getPrice());
        holder.productTotalPrice.setText("Total :\t" + cartProductsList.get(position).getTotal());


        applyClickEvents(holder, position);

    }

    @Override
    public int getItemCount() {
        return cartProductsList.size();
    }


    private void applyClickEvents(final CartProductAdapter.CartProductViewHolder holder, final int position) {

        holder.productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemListener.onClick(position);

                cartProductsList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), cartProductsList.size());

            }
        });


        holder.productQtyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartQtyClickListner.onClick(position);

            }
        });

        holder.productSubtractQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subtractCartQtyClickListner.onClick(position);

            }
        });

    }

}
