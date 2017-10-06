package com.NamohTours.View;

import android.text.Html;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.NamohTours.R;
import com.NamohTours.expandablerecyclerview.models.ExpandableGroup;
import com.NamohTours.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by ubuntu on 11/7/17.
 */

public class HeaderViewholder extends GroupViewHolder {

    private TextView txtHeader;
    private ImageView arraowImage;
    private ImageView icon;
    private String groupHeading;

    public HeaderViewholder(View itemView) {
        super(itemView);

        txtHeader = (TextView) itemView.findViewById(R.id.lblListHeader);
        arraowImage = (ImageView) itemView.findViewById(R.id.list_item_arrow);
        icon = (ImageView) itemView.findViewById(R.id.lblListIcon);
    }


    public void setGroupName(ExpandableGroup group) {
        txtHeader.setText(Html.fromHtml(group.getTitle()));

        groupHeading = group.getTitle();

        if (groupHeading.equals("Itinerary")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_itinerary));
        }

        if (groupHeading.contains("Flight")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_nav_flight));
        }
        if (groupHeading.equals("Departure Dates")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_dates));
        }
        if (groupHeading.equals("Tour Info")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_tour));
        }
        if (groupHeading.contains("Season")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_currancy));
        }
        if (groupHeading.contains("Things to carry")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_things));
        }
        if (groupHeading.contains("Tour Terms")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_terms));
        }

        if (groupHeading.contains("Inclusions")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_inclusion));
        }

// FOR EVENT
        if (groupHeading.contains("Time")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_dates));
        }

        if (groupHeading.contains("Event")) {
            icon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_extra_itinerary));
        }

    }


    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
       /* RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arraowImage.setAnimation(rotate);*/
    }

    private void animateCollapse() {
       /* RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arraowImage.setAnimation(rotate);*/
    }

}