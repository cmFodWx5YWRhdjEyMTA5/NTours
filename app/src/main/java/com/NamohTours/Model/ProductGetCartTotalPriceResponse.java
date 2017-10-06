package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 1/9/17.
 */

public class ProductGetCartTotalPriceResponse {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("value")
    private String value;

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }



    /*RESPONSE
    *
    * "totals": [
      {
        "title": "Sub-Total",
        "text": "₹117,180",
        "value": 117180
      },
      {
        "title": "CGST(2.5%)",
        "text": "₹1,200",
        "value": 1199.5
      },
      {
        "title": "SGST(2.5%)",
        "text": "₹1,200",
        "value": 1199.5
      },
      {
        "title": "Total",
        "text": "₹119,579",
        "value": 119579
      }
    ],
    *
    *
    * */
}
