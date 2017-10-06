package com.NamohTours.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.NamohTours.Interface.ClickListener;
import com.NamohTours.Model.GetUserUploadDocumentsDetails;
import com.NamohTours.R;

import java.io.File;
import java.util.List;

import static com.NamohTours.Service.Prefs.Register_Preference;
import static com.NamohTours.Service.Prefs.UserID;

/**
 * Created by Pooja Mantri on 6/10/17.
 */

public class OrderIdUplodedDocsAdapter extends RecyclerView.Adapter<OrderIdUplodedDocsAdapter.CustDocsViewHolder> {


    private Context context;
    private List<GetUserUploadDocumentsDetails> DocsDetailList;
    private int rowLayout;
    private ClickListener clickListener;
    private SharedPreferences RegisterPrefences;
    private String orderId;


    public static class CustDocsViewHolder extends RecyclerView.ViewHolder {


        TextView productTitle;
        ImageView docsDownlaod, docsView;

        public CustDocsViewHolder(View itemView) {
            super(itemView);
            productTitle = (TextView) itemView.findViewById(R.id.txtDocsName);
            docsDownlaod = (ImageView) itemView.findViewById(R.id.docsDownload);
            docsView = (ImageView) itemView.findViewById(R.id.docsView);

        }
    }

    public OrderIdUplodedDocsAdapter(List<GetUserUploadDocumentsDetails> responseList, int rowLayout, Context context, ClickListener clickListener) {
        this.DocsDetailList = responseList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public OrderIdUplodedDocsAdapter.CustDocsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new OrderIdUplodedDocsAdapter.CustDocsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderIdUplodedDocsAdapter.CustDocsViewHolder holder, int position) {


        orderId = String.valueOf(DocsDetailList.get(position).getOrder_id());

        holder.productTitle.setText(DocsDetailList.get(position).getMask());

        // WE Create folder Link Namohtours/Customer_Id(e.g -155)/DocumentName
        // Document Name will be Upload id_ImageName for --  Same image can be downloaded
        String path = Environment.getExternalStorageDirectory() + "/" + "NamohDocs/Order/" + orderId;

        File fo = new File(path);
        if (!fo.exists()) {
            fo.mkdirs();
        }
        // Output stream


        String tempName = DocsDetailList.get(position).getUpload_id() + "_" + DocsDetailList.get(position).getMask();
        File newFile = new File(path, tempName);

        // WE check here that document is exists or not in Folder -- for change button
        // If not Exists then show Download button
        // Else show view button
        if (!newFile.exists()) {
            holder.docsView.setVisibility(View.INVISIBLE);
        } else {
            holder.docsView.setVisibility(View.VISIBLE);
            holder.docsDownlaod.setVisibility(View.INVISIBLE);
        }


        applyClickEvents(holder, position);

    }

    @Override
    public int getItemCount() {
        return DocsDetailList.size();
    }


    private void applyClickEvents(final OrderIdUplodedDocsAdapter.CustDocsViewHolder holder, final int position) {


        if (holder.docsDownlaod.getVisibility() == View.INVISIBLE) {
            holder.docsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onClick(position);
                }
            });
        } else {
            holder.docsDownlaod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onClick(position);
                }
            });
        }


    }


}
