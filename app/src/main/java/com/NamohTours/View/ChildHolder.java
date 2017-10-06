package com.NamohTours.View;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.NamohTours.R;
import com.NamohTours.expandablerecyclerview.models.ExpandableGroup;
import com.NamohTours.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by ubuntu on 11/7/17.
 */

public class ChildHolder extends ChildViewHolder {

    private WebView webView;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    public ChildHolder(View itemView) {
        super(itemView);


        webView = (WebView) itemView.findViewById(R.id.lblListItem);

    }

    public void onBind(ProductExtraTabHeader header, ExpandableGroup group) {


        //ViewCompat.setNestedScrollingEnabled(webView,false);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.loadData(header.getTitle(), mimeType, encoding);


    }
}
