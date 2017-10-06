package com.NamohTours.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Activity.OrderHistoryDetailActivity;
import com.NamohTours.Model.ConfirmOrderProductResponse;
import com.NamohTours.Model.OrderHistoryOptionResponse;
import com.NamohTours.Model.OrderHistoryProductResponse;
import com.NamohTours.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pooja Mantri on 26/9/17.
 */

public class OrderHistoryDetailViewAdapter extends RecyclerView.Adapter<OrderHistoryDetailViewAdapter.OHDetailViewHolder> {

    private Context context;
    private List<OrderHistoryProductResponse> OHProductList;
    private int rowLayout;
    private List<OrderHistoryOptionResponse> optionsList;


    public static class OHDetailViewHolder extends RecyclerView.ViewHolder {


        TextView productTitle, productPrice, productTotalPrice;
        LinearLayout LLOptions;

        public OHDetailViewHolder(View itemView) {
            super(itemView);
            productTitle = (TextView) itemView.findViewById(R.id.OrderHProductTitle);
            productPrice = (TextView) itemView.findViewById(R.id.OrderHProductPriceTitle);
            productTotalPrice = (TextView) itemView.findViewById(R.id.OrderHProductTotalPrice);
            LLOptions = (LinearLayout) itemView.findViewById(R.id.LLOHOptions);
        }
    }

    public OrderHistoryDetailViewAdapter(List<OrderHistoryProductResponse> responseList, int rowLayout, Context context) {
        this.OHProductList = responseList;
        this.rowLayout = rowLayout;
        this.context = context;
    }


    @Override
    public OHDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new OrderHistoryDetailViewAdapter.OHDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OHDetailViewHolder holder, int position) {

        holder.productTitle.setText(OHProductList.get(position).getName());
        holder.productPrice.setText(OHProductList.get(position).getQuantity() + "\t\tX\t\t" + OHProductList.get(position).getPrice());
        holder.productTotalPrice.setText(OHProductList.get(position).getTotal());


        optionsList = new ArrayList<OrderHistoryOptionResponse>();
        optionsList = OHProductList.get(position).getOption();

        if ((optionsList.size() > 0) && (optionsList != null)) {
            for (int i = 0; i < optionsList.size(); i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(5, 5, 5, 5);

                // FOR LABEL
                TextView[] txt = new TextView[2];

                txt[0] = new TextView(context);
                txt[0].setText(optionsList.get(i).getName());
                txt[0].setTypeface(null, Typeface.BOLD);
                txt[0].setTag(optionsList.get(i).getOption_id());

                txt[1] = new TextView(context);

                txt[1].setText(optionsList.get(i).getValue());
                txt[1].setTag(optionsList.get(i).getOption_value_id());

                holder.LLOptions.addView(txt[0], layoutParams);
                holder.LLOptions.addView(txt[1], layoutParams);


            }
        }




    }

    @Override
    public int getItemCount() {
        return OHProductList.size();
    }



}
