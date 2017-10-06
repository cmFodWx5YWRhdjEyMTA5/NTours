package com.NamohTours.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NamohTours.Model.ProductReviewDetailListResponse;
import com.NamohTours.R;
import com.NamohTours.View.ReadMoreTextView;


import java.util.List;

/**
 * Created by Pooja Mantri on 27/7/17.
 */

public class ProductUserReviewAdapter extends RecyclerView.Adapter<ProductUserReviewAdapter.Review> {

    private List<ProductReviewDetailListResponse> responseList;
    private int rowLayout;
    private Context context;


    public class Review extends RecyclerView.ViewHolder {

        LinearLayout linearProductReview;
        TextView txtProductReviewUserName, txtProductReviewRating, txtProductReviewDate, txtProductReviewDetail;

        public Review(View itemView) {
            super(itemView);

            linearProductReview = (LinearLayout) itemView.findViewById(R.id.linearProductReview);

            txtProductReviewUserName = (TextView) itemView.findViewById(R.id.txtProductReviewAuthor);
            txtProductReviewRating = (TextView) itemView.findViewById(R.id.txtProductReviewRating);
            txtProductReviewDetail = (TextView) itemView.findViewById(R.id.txtProductReviewDetail);
            txtProductReviewDate = (TextView) itemView.findViewById(R.id.txtProductReviewDate);


        }
    }


    public ProductUserReviewAdapter(List<ProductReviewDetailListResponse> responseList, int rowLayout, Context context) {
        this.responseList = responseList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Review onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, null, false);
        return new ProductUserReviewAdapter.Review(view);

    }

    @Override
    public void onBindViewHolder(Review holder, int position) {

        holder.txtProductReviewUserName.setText(responseList.get(position).getAuthor());
        holder.txtProductReviewRating.setText(responseList.get(position).getRating());
        holder.txtProductReviewDate.setText(responseList.get(position).getDate_added());
        holder.txtProductReviewDetail.setText(Html.fromHtml(responseList.get(position).getText()));


    }


    @Override
    public int getItemCount() {
        return responseList.size();
    }

}
