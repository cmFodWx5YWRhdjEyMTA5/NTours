package com.NamohTours.rest;

import com.NamohTours.Model.ApplyCouponCodeOnOrder;
import com.NamohTours.Model.ApplyVoucherCodeOnOrder;
import com.NamohTours.Model.ChangePasswordFields;
import com.NamohTours.Model.ChangePasswordResponse;
import com.NamohTours.Model.CoampreProductResponse;
import com.NamohTours.Model.ComapreProductDetailResponse;
import com.NamohTours.Model.ConfirmOrderProductResponse;
import com.NamohTours.Model.ConfirmOrderResponse;
import com.NamohTours.Model.DeleteCartProductResponse;
import com.NamohTours.Model.ForgetPasswordResponse;
import com.NamohTours.Model.GetPaymentMethodResponse;
import com.NamohTours.Model.GetUserOrderHistory;
import com.NamohTours.Model.GetUserPaymentAddress;
import com.NamohTours.Model.GetUserUploadDocumentsByOrderIdResponse;
import com.NamohTours.Model.GetUserUploadDocumentsResponse;
import com.NamohTours.Model.GetWishlistProductResponse;
import com.NamohTours.Model.OrderCashChequePaymentMethodResponse;
import com.NamohTours.Model.OrderHistoryMainResponse;
import com.NamohTours.Model.OrderPayResponse;
import com.NamohTours.Model.PostPaymentMethodOption;
import com.NamohTours.Model.PostUserPaymentAddressResponse;
import com.NamohTours.Model.ProductAddCartDetails;
import com.NamohTours.Model.ProductAddCartResponse;
import com.NamohTours.Model.ProductGetCartResponse;
import com.NamohTours.Model.SlideShowResponse;
import com.NamohTours.Model.TokenResponse;
import com.NamohTours.Model.TourCategoryFilterResponse;
import com.NamohTours.Model.TourCategoryResponse;
import com.NamohTours.Model.TourFeaturedProductResponse;
import com.NamohTours.Model.TourProductResponse;
import com.NamohTours.Model.TourProductResponseById;
import com.NamohTours.Model.UpdateCartProductQuantity;
import com.NamohTours.Model.UserAccountResponse;
import com.NamohTours.Model.UserEditProfileResponse;
import com.NamohTours.Model.UserLoginFields;
import com.NamohTours.Model.UserLoginRegisterDetailResponse;
import com.NamohTours.Model.UserLoginRegisterResponse;
import com.NamohTours.Model.MoviesResponse;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


// REFERNCE DOCUMNET
/*
REST API Oauth Document : http://opencartoauth.opencart-api.com/schema_v2.0_oauth/
* */
public interface ApiInterface {


    // for Get Authorised token  pass base64 encoded clientid:client secret
    // e.g. for access api pass encoded clientid:clientsecret with this method
    //  Basic ZGVtb19vYXV0aF9jbGllbnQ6ZGVtb19vYXV0aF9zZWNyZXQ=  and it will send token


    ///oauth2/token/{grant_type}
    @POST("oauth2/token/client_credentials")
    Call<TokenResponse> getToken(@Header("Authorization") String token);

    // get New token in replace of old Token
    @POST("oauth2/token/client_credentials")
    Call<TokenResponse> getRefreshToken(@Header("Authorization") String token, @Body TokenResponse oldToken);







    /*
    * "address_1": "Test street 88",
	"city": "Berlin",
	"country_id": "97",
	"email": "sampletest1@gmail.com",
	"firstname": "sampler",
	"lastname": "tester",
	"postcode": "1111",
	"telephone": "+36306668884",
	"zone_id": "1433",
	"password": "12345",
	"confirm": "12345",
	"agree": "1"
    * */

    // for regsiter user
    // @FormUrlEncoded
    @POST("register")
    Call<UserLoginRegisterResponse> registeruser(@Header("Authorization") String token, @Body UserLoginRegisterDetailResponse registerResponse);


    // for Login User who registred with site
    /* Token Header And
    * {
	"email":"test@test.com",
	"password":"12345"
}*/

    @POST("login")
    Call<UserLoginRegisterResponse> loginuser(@Header("Authorization") String token, @Body UserLoginFields userLoginFields);


    // Logout User
    @POST("logout")
    Call<UserLoginRegisterResponse> logoutUser(@Header("Authorization") String token);


    // chnage password
    @POST("account/password")
    Call<ChangePasswordResponse> changePassword(@Header("Authorization") String token, @Body ChangePasswordFields chnagePasswordFields);


    // forget password

    @POST("forgotten")
    Call<ForgetPasswordResponse> forgottenPassword(@Header("Authorization") String token, @Body ForgetPasswordResponse forgetemail);


    // GET User Account Details
    @GET("account")
    Call<UserAccountResponse> getAccountDetails(@Header("Authorization") String token);

    @POST("account")
    Call<UserEditProfileResponse> editProfile(@Header("Authorization") String token, @Body UserEditProfileResponse editProfile);


    // All Products Sort

    @GET("products/sort/name/order/asc")
    Call<TourProductResponse> getSortedProductByNameAsc(@Header("Authorization") String token);


    @GET("products/sort/name/order/desc")
    Call<TourProductResponse> getSortedProductByNameDesc(@Header("Authorization") String token);


    @GET("products/sort/price/order/asc")
    Call<TourProductResponse> getSortedProductByPriceAsc(@Header("Authorization") String token);

    @GET("products/sort/price/order/desc")
    Call<TourProductResponse> getSortedProductByPriceDesc(@Header("Authorization") String token);





    /*Product Sort , Order ,Filter , Limit , Search , category ,Demo urls
    // Parameters key and Values
    // 1.sort = name , price
    // 2.Order = Ascending , Descending
    // 3. Filter =
    // 4. Search = (search keyword, search in product name, model, sku, upc, ean, jan, isbn, mpn)
    // 5. Cataegory=  Category id
     //6.

    sort by name
 I>> Get list of products.
             // sort by A-Z
             http://opencartoauth.opencart-api.com/api/rest/products/sort/name/order/asc OR
    * 1. http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&sort=name&order=asc
    *
    *    // sort by Z-A
    *    http://opencartoauth.opencart-api.com/api/rest/products/sort/name/order/desc OR
    * 2.http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&sort=name&order=desc
    *
    *   // sort by Price Asc -- Low to High
    *   http://opencartoauth.opencart-api.com/api/rest/products/sort/price/order/asc OR
    *   3.http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&sort=price&order=asc
    *
    *   // sort by Price Desc --- High to Low
    *    4. http://opencartoauth.opencart-api.com/api/rest/products/sort/price/order/desc OR
     *   4.http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&sort=price&order=desc
     *
     * *

  II>> Get simple list of products. It is faster than previous one, but it returns limited product informations.

        1. http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&simple
        2.http://opencartoauth.opencart-api.com/index.php?route=feed/rest_api/products&simple=1

  III>> FOR mORE Documentation
        Visit this Url : http://opencartoauth.opencart-api.com/schema_v2.0_oauth/

    * */


    @GET("featured")
    Call<TourFeaturedProductResponse> getFeaturedProduct(@Header("Authorization") String token);


    // GET All Categories for 2 level
    @GET("categories/level/2")
    Call<TourCategoryResponse> getTourCategory(@Header("Authorization") String token);


    @GET("categories/{catergory_id}")
    Call<TourCategoryFilterResponse> getCategorybyId(@Header("Authorization") String token, @Path("catergory_id") Integer Category_id);


    // GET Products by Category Id
    @GET("products/category/{category_id}")
    Call<TourProductResponse> getTourPrdocutByCategoryId(@Header("Authorization") String token, @Path("category_id") Integer category_id);


    // GET products by Product Id

    @GET("products/{product_id}")
    Call<TourProductResponseById> getProductbyId(@Header("Authorization") String token, @Path("product_id") Integer product_id);

    // GET all products
    @GET("products")
    Call<TourProductResponse> getTourProduct(@Header("Authorization") String token);


    @GET("compare/{ids}")
    Call<CoampreProductResponse> getCompareProduct(@Header("Authorization") String token, @Path("ids") String productIds);

    @GET("wishlist")
    Call<GetWishlistProductResponse> getWishListProduct(@Header("Authorization") String token);


    @GET("products/search/{search}")
    Call<TourProductResponse> getSearchProduct(@Header("Authorization") String token, @Path("search") String searchQuery);


    // get sub categories by selecting parent category -- domestic --> kerala,himchal,

    //http://namohtours.com/api/rest/categories/parent/145 -- it gives minimum details of sub category

    @GET("categories/parent/{parent_id}")
    Call<TourCategoryResponse> getSubCategories(@Header("Authorization") String token, @Path("parent_id") Integer parent_id);


    @GET("products/category/{category_id}/filters/{filters}")
    Call<TourProductResponse> getFiltersProducts(@Header("Authorization") String token, @Path("category_id") Integer category_id, @Path("filters") String filters);

    // Products sort Under Categories
    @GET("products/category/{category_id}/sort/name/order/asc")
    Call<TourProductResponse> getSortedCategoryProductByNameAsc(@Header("Authorization") String token, @Path("category_id") Integer category_id);


    @GET("products/category/{category_id}/sort/name/order/desc")
    Call<TourProductResponse> getSortedCategoryProductByNameDesc(@Header("Authorization") String token, @Path("category_id") Integer category_id);


    @GET("products/category/{category_id}/sort/price/order/asc")
    Call<TourProductResponse> getSortedCategoryProductByPriceAsc(@Header("Authorization") String token, @Path("category_id") Integer category_id);

    @GET("products/category/{category_id}/sort/price/order/desc")
    Call<TourProductResponse> getSortedCategoryProductByPriceDesc(@Header("Authorization") String token, @Path("category_id") Integer category_id);

    @GET("slideshows")
    Call<SlideShowResponse> getSlides(@Header("Authorization") String token);


    @GET("index.php?route=feed/rest_api/products")
    Call<MoviesResponse> getTopRatedMovies(@Query("key") String apiKey);


    @POST("cart")
    Call<ProductAddCartResponse> addToCart(@Header("Authorization") String token, @Body ProductAddCartDetails cartDetails);


    @GET("cart")
    Call<ProductGetCartResponse> getCartProducts(@Header("Authorization") String token);

    // Delete method not working so we change in api code and make it as a post method
    @POST("cart")
    Call<DeleteCartProductResponse> deleteCartProducts(@Header("Authorization") String token, @Body DeleteCartProductResponse productKey);


    // Update product quantity using PUT bt put method not working so , we made byt bpost method and make changes in cart.php in api
    @POST("cart/update")
    Call<UpdateCartProductQuantity> updateCartQty(@Header("Authorization") String token, @Body UpdateCartProductQuantity updateCart);


    // Payment Address Get

    @GET("paymentaddress")
    Call<GetUserPaymentAddress> getUserPaymentAddress(@Header("Authorization") String token);


    @POST("paymentaddress")
    Call<PostUserPaymentAddressResponse> postUserPaymentAddress(@Header("Authorization") String token, @Body PostUserPaymentAddressResponse existingAddress);


    @GET("paymentmethods")
    Call<GetPaymentMethodResponse> getPaymentMethodResponse(@Header("Authorization") String token);

    @POST("paymentmethods")
    Call<PostPaymentMethodOption> postPaymentMethod(@Header("Authorization") String token, @Body PostPaymentMethodOption postPaymentMethodOption);


    // Confirm order response

    @POST("confirm")
    Call<ConfirmOrderResponse> ConfirmOrderRes(@Header("Authorization") String token);


    @POST("confirm")
    Call<OrderCashChequePaymentMethodResponse> payWeb(@Header("Authorization") String token, @Body OrderCashChequePaymentMethodResponse orderResponse);


    // Apply VoucherCode
    @POST("voucher")
    Call<ApplyVoucherCodeOnOrder> voucherCode(@Header("Authorization") String token, @Body ApplyVoucherCodeOnOrder voucherCode);


    @POST("coupon")
    Call<ApplyCouponCodeOnOrder> couponCode(@Header("Authorization") String token, @Body ApplyCouponCodeOnOrder couponCode);


    @GET("orders/user/{customer_id}")
    Call<GetUserOrderHistory> userOrderHistory(@Header("Authorization") String token, @Path("customer_id") String customer_id);


    @GET("orders/{order_id}")
    Call<OrderHistoryMainResponse> orderHistorybyOrderId(@Header("Authorization") String token, @Path("order_id") String customer_id);


    // Get Uploaded documents List by customer id
    @POST("account")
    Call<GetUserUploadDocumentsResponse> getUserDocumentListByCustId(@Header("Authorization") String token, @Body GetUserUploadDocumentsResponse custId);


    // Get Uploaded documents List by Order Id
    @POST("customerorders")
    Call<GetUserUploadDocumentsByOrderIdResponse> getUserDocumentListByOrderId(@Header("Authorization") String token, @Body GetUserUploadDocumentsByOrderIdResponse orderId);
}
