package com.NamohTours.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja Mantri on 11/9/17.
 */

public class OrderPayResponse {


    public class Example {

        @SerializedName("success")

        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }


    }

}
