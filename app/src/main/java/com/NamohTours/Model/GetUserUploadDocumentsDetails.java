package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 3/10/17.
 */

public class GetUserUploadDocumentsDetails {

    @SerializedName("upload_id")
    private String upload_id;

    @SerializedName("filename")
    private String filename;

    @SerializedName("mask")
    private String mask;

    @SerializedName("date_added")
    private String date_added;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("order_id")
    private Integer order_id;


    private String fullFileName;

    public String getUpload_id() {
        return upload_id;
    }


    public String getFilename() {
        return filename;
    }


    public String getMask() {
        return mask;
    }


    public String getDate_added() {
        return date_added;
    }


    public String getCustomer_id() {
        return customer_id;
    }


    public Integer getOrder_id() {
        return order_id;
    }


    public String getFullFileName() {

        String temp = getFilename();

        temp = temp.substring(3);

        temp = "http://namohtours.com/system/" + temp;
        fullFileName = temp;

        return fullFileName;
    }
}
