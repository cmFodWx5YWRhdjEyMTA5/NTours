package com.NamohTours.Model;

/**
 * Created by ubuntu on 18/4/17.
 */

public class InternationalTourList {

    private String InternationalTourHeading;

    private int IntrnationalImage;

    public InternationalTourList(String InterHeading, int imageResource) {

        this.InternationalTourHeading = InterHeading;
        this.IntrnationalImage = imageResource;
    }

    public void setInternationalTourHeading(String internationalTourHeading) {
        InternationalTourHeading = internationalTourHeading;
    }

    public String getInternationalTourHeading() {
        return InternationalTourHeading;
    }


    public int getIntrnationalImage() {
        return IntrnationalImage;
    }

    public void setIntrnationalImage(int intrnationalImage) {
        IntrnationalImage = intrnationalImage;
    }
}
