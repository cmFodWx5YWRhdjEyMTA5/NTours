package com.NamohTours.View;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ubuntu on 11/7/17.
 */

public class ProductExtraTabHeader implements Parcelable {


    private String title;


    public ProductExtraTabHeader(Parcel in) {
        title = in.readString();
    }


    public ProductExtraTabHeader(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Creator<ProductExtraTabHeader> CREATOR = new Creator<ProductExtraTabHeader>() {
        @Override
        public ProductExtraTabHeader createFromParcel(Parcel in) {
            return new ProductExtraTabHeader(in);
        }

        @Override
        public ProductExtraTabHeader[] newArray(int size) {
            return new ProductExtraTabHeader[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
    }
}
