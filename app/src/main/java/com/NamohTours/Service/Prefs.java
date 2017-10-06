package com.NamohTours.Service;

/**
 * Created by ubuntu on 10/5/17.
 */

public class Prefs {

    // Base 64 encoded clientId:clientSecret string into following and add basic after encoded string
    // Client Id:namohclient
    // client Secret :nimish1212
    // ClientId:client Secret --  namohclient:nimish1212
    // Encoded  this string for token in BASE 64 : namohclient:nimish1212
    public static final String AUTHORIZATION = "Basic bmFtb2hjbGllbnQ6bmltaXNoMTIxMg==";
    public static final String MY_PREFS_NAME = "SHARED";
    public static final String TOKEN_KEY = "TOKEN";
    public static final String TAB = "TAB";
    public static final String Register_Preference = "RegisterUser";
    public static final String UserRegister = "UserRegistred";
    public static final String UserName = "Name";
    public static final String UserContact = "Contact";
    public static final String UserEmail = "Email";
    public static final String UserID = "CUSTID";

    public static final String FIREBASEREGID = "FireBRegId";

    public static final String TOKEN_EXPIRY = "EXPIRY";

    public static final String PAYMENT = "payment";

}
