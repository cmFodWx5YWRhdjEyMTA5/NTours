package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Interface.WishListClick;
import com.NamohTours.Model.GetUserOrderHistoryDetail;
import com.NamohTours.Model.ProductGetCartProductsList;
import com.NamohTours.R;

import java.util.List;

/**
 * Created by Pooja Mantri on 22/9/17.
 */

public class UserOrderHistoryAdapter extends RecyclerView.Adapter<UserOrderHistoryAdapter.UserOrderHistoryViewHolder> {


    private Context context;
    private List<GetUserOrderHistoryDetail> orderHistoryDetailList;
    private int rowLayout;
    private ClickListener ItemListener;
    private WishListClick clickListner;
    private String price, temp;

    public static class UserOrderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderId, txtOrderStatus, txtDateAdded, txtNoOfProducts, txtTotalPrice;
        Button btnOrderView, btnDocsView;

        public UserOrderHistoryViewHolder(View itemView) {
            super(itemView);

            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderID);
            txtOrderStatus = (TextView) itemView.findViewById(R.id.txtOrderStatus);
            txtDateAdded = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtNoOfProducts = (TextView) itemView.findViewById(R.id.txtNoOfProducts);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtOrderPriceTotal);

            btnOrderView = (Button) itemView.findViewById(R.id.btnOrderView);
            btnDocsView = (Button) itemView.findViewById(R.id.btnOrderDocs);

        }
    }

    public UserOrderHistoryAdapter(List<GetUserOrderHistoryDetail> orderHistoryDetails, int rowLayout, Context context, ClickListener itemListener, WishListClick clickListner) {
        this.orderHistoryDetailList = orderHistoryDetails;
        this.rowLayout = rowLayout;
        this.context = context;
        this.ItemListener = itemListener;
        this.clickListner = clickListner;
    }

    @Override
    public UserOrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new UserOrderHistoryAdapter.UserOrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserOrderHistoryViewHolder holder, int position) {


        holder.txtOrderId.setText(orderHistoryDetailList.get(position).getOrder_id());
        holder.txtDateAdded.setText(orderHistoryDetailList.get(position).getDate_added());
        holder.txtNoOfProducts.setText(orderHistoryDetailList.get(position).getProducts());
        holder.txtOrderStatus.setText(orderHistoryDetailList.get(position).getStatus());

        temp = orderHistoryDetailList.get(position).getTotal();

        // Get Price before Dot(.) and add rupee symbol
        price = temp.substring(0, temp.indexOf("."));
        holder.txtTotalPrice.setText("\u20B9" + price);

        applyClickEvents(holder, position);

    }

    @Override
    public int getItemCount() {
        return orderHistoryDetailList.size();
    }

    private void applyClickEvents(final UserOrderHistoryAdapter.UserOrderHistoryViewHolder holder, final int position) {

        holder.btnOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemListener.onClick(position);
            }
        });


        holder.btnDocsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListner.onClick(position);
            }
        });




    }
}
