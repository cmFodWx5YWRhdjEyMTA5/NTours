package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.NamohTours.Model.ConfirmOrderProductResponse;
import com.NamohTours.Model.ProductGetCartProductsList;
import com.NamohTours.R;

import java.util.List;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.OrderProductViewHolder> {


    private Context context;
    private List<ConfirmOrderProductResponse> confirmOrderProductResponses;
    private int rowLayout;


    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {


        TextView productTitle, productPrice, productTotalPrice;


        public OrderProductViewHolder(View itemView) {
            super(itemView);


            productTitle = (TextView) itemView.findViewById(R.id.OrderProductTitle);
            productPrice = (TextView) itemView.findViewById(R.id.OrderProductPriceTitle);
            productTotalPrice = (TextView) itemView.findViewById(R.id.OrderProductTotalPrice);


        }
    }


    public ConfirmOrderAdapter(List<ConfirmOrderProductResponse> responseList, int rowLayout, Context context) {
        this.confirmOrderProductResponses = responseList;
        this.rowLayout = rowLayout;
        this.context = context;

    }


    @Override
    public ConfirmOrderAdapter.OrderProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ConfirmOrderAdapter.OrderProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OrderProductViewHolder holder, int position) {

        holder.productTitle.setText(confirmOrderProductResponses.get(position).getName());
        holder.productPrice.setText(confirmOrderProductResponses.get(position).getQuantity() + "\t\tX\t\t" + confirmOrderProductResponses.get(position).getPrice());
        holder.productTotalPrice.setText(confirmOrderProductResponses.get(position).getTotal());

    }

    @Override
    public int getItemCount() {
        return confirmOrderProductResponses.size();
    }


}
