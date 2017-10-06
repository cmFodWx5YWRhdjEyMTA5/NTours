package com.NamohTours.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ubuntu on 5/5/17.
 */

public class UserLoginRegisterResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private UserLoginRegisterDetailResponse warning;

    @SerializedName("data")
    private UserLoginRegisterDetailResponse data;

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("statusText")
    private String statusText;

    @SerializedName("error_description")
    private String error_description;

    public UserLoginRegisterDetailResponse getData() {
        return data;
    }

    public void setData(UserLoginRegisterDetailResponse data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public UserLoginRegisterDetailResponse getWarning() {
        return warning;
    }

    public void setWarning(UserLoginRegisterDetailResponse warning) {
        this.warning = warning;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getError_description() {
        return error_description;
    }
    // following are the response for user register

    // Response if user registered sucessfully
    /*
    *

    {
        "success": true,
        "data":
        {
            "city": "Berlin",
            "email": "chetana112@gmail.com",
            "firstname": "chetan",
            "lastname": "tanpure",
            "address_1": "Ahmednagar",
            "country_id": "97",
            "postcode": "12345",
            "telephone": "+9151234567",
            "zone_id": "1433",
            "customer_id": 47,
            "address_id": "57"
        }
    }


*/

    // Response if user email is alrady registerd
    /*
    *

    {
        "success": false,
        "error":
        {
            "warning": "Warning: E-Mail Address is already registered!"
        }
    }


    * */


    // Response if user is already logged in to site

    /*
    *

    {
        "success": false,
        "error": "User already is logged"
    }

*/


    // if any error
    /*
    *

    {
        "statusCode": 400,
        "statusText": "Bad Request",
        "error_description": "Malformed auth header"
    }

*/


    // if without token
    /*
    *

    {
        "statusCode": 401,
        "statusText": "Unauthorized",
        "error_description": null
    }



*/
}
