package com.NamohTours.Model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pooja Mantri on 25/9/17.
 */

public class ProductDescriptionOptionsAddtoCart {


    private HashMap<String, String> radio;

    private HashMap<String, List<String>> checkbox;


    public ProductDescriptionOptionsAddtoCart(HashMap<String, String> radio, HashMap<String, List<String>> checkbox) {
        this.radio = radio;

        this.checkbox = checkbox;
    }

    public HashMap<String, String> getRadio() {
        return radio;
    }

    public void setRadio(HashMap<String, String> radio) {
        this.radio = radio;
    }

    public HashMap<String, List<String>> getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(HashMap<String, List<String>> checkbox) {
        this.checkbox = checkbox;
    }
}
