package com.dtag.osta.network.Interface;

import com.dtag.osta.network.ResponseModel.Model.PhoneNumber;
import com.dtag.osta.network.ResponseModel.Model.agent.Agent;
import com.dtag.osta.network.ResponseModel.Model.common.ChangePassword;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.Model.common.RateApp;
import com.dtag.osta.network.ResponseModel.Model.common.ResetPassword;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.Model.user.CancelOrder;
import com.dtag.osta.network.ResponseModel.Model.user.RateService;
import com.dtag.osta.network.ResponseModel.Model.user.Registeration;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.SetToken;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    //{{base}}/api/categories
    @GET("api/categories")
    Call<ApiResponse> serviceCategories();

    //{{base}}/api/user/order/list?status=new
    @GET("api/user/order/list")
    Call<ApiResponse> userRequestsList(@Header("token") String token, @Query("status[]") String status[]);

    //{{base}}/api/agent/order/list?status=new
    @GET("api/agent/order/list")
    Call<ApiResponse> agentRequestsList(@Header("token") String token, @Query("status[]") String status[]);

    //{{base}}/api/user/order/show?order_id=1
    @GET("api/user/order/show")
    Call<ApiResponse> userOrderDetails(@Header("token") String token, @Query("order_id") int id);

    //{{base}}/api/agent/order/show?order_id=1
    @GET("api/agent/order/show")
    Call<ApiResponse> agentOrderDetails(@Header("token") String token, @Query("order_id") int id);

    //{{base}}/api/user/register/verify_email
    @FormUrlEncoded
    @POST("api/user/register/verify_email")
    Call<ApiResponse> sendPhoneNumber(@Field("email") String email);

    //{{base}}/api/agent/register/verify_email
    @FormUrlEncoded
    @POST("api/agent/register/verify_email")
    Call<ApiResponse> agentSendPhoneNumber(@Field("email") String email);

    //{{base}}/api/user/register/verify_code
    @POST("api/user/register/verify_code")
    Call<ApiResponse> verifyCode(@Body PhoneNumber phoneNumber);

    //{{base}}/api/cities
    @GET("api/cities")
    Call<ApiResponse> getCities();

    //{{base}}/api/city?city_id=1
    @GET("api/city")
    Call<ApiResponse> getCity(@Query("city_id") int cityId);

    //{{base}}/api/user/register
    @POST("api/user/register")
    Call<ApiResponse> userRegister(@Body Registeration registeration);

    //{{base}}/api/agent/register
    @Multipart
    @POST("api/agent/register")
    Call<ApiResponse> agentRegister(@Part("code") RequestBody code,
                                    @Part("name") RequestBody name,
                                    @Part("phone") RequestBody phone,
                                    //  @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part("password_confirmation") RequestBody password_confirmation,
//                                    @Part("national_id2") RequestBody national_id2,
                                    @Part("birthday") RequestBody birthday,
                                    @Part("category_id") RequestBody category_id,
                                    @Part("city_id") RequestBody city_id,
                                    @Part("longitude") RequestBody longitude,
                                    @Part("latitude") RequestBody latitude,
//                                    @Part List<MultipartBody.Part> national_id,
                                    @Part List<MultipartBody.Part> documents_images,
                                    @Part MultipartBody.Part image
    );

    //{{base}}/api/user/profile
    @GET("api/user/profile")
    Call<ApiResponse> getCustomerProfile(@Header("token") String token);

    //{{base}}/api/agent/profile
    @GET("api/agent/profile")
    Call<ApiResponse> getAgentProfile(@Header("token") String token);

    //{{base}}/api/agent/profile
    @POST("api/agent/profile")
    Call<ApiResponse> updateAgentProfile(@Header("token") String token, @Body Agent agent);

    //{{base}}/api/user/profile
    @POST("api/user/profile")
    Call<ApiResponse> updateUserProfile(@Header("token") String token, @Body Registeration registeration);

    //{{base}}/api/login
    @POST("api/login")
    Call<ApiResponse> logIn(@Body LoginRequest loginRequest);

    //{{base}}/api/user/order
    //{{base}}/api/user/order
    @Multipart
    @POST("api/user/order")
    Call<ApiResponse> makeOrder(@Header("token") String token,
                                @Part("category_id") RequestBody category_id,
//                                @Part("address") RequestBody address,
                                @Part("city_id") RequestBody city_id,
                                @Part("coupon") RequestBody coupon,
                                @Part("latitude") RequestBody latitude,
                                @Part("longitude") RequestBody longitude,
                                @Part("date") RequestBody date,
                                @Part("time") RequestBody time,
                                @Part("description") RequestBody description,
                                @Part List<MultipartBody.Part> images,
                                @Part("payment_method") RequestBody payment_method);


    //{{base}}/api/page?name=about
    @GET("api/page")
    Call<ApiResponse> aboutApp(@Query("name") String name);

    //{{base}}/api/user/order/show?order_id=35
    @GET("api/user/order/show")
    Call<ApiResponse> showOrder(@Header("token") String token, @Query("order_id") int orderId);

    //{{base}}/api/user/password
    @POST("api/user/password")
    Call<ApiResponse> changePassword(@Header("token") String token, @Body ChangePassword changePassword);

    //{{base}}/api/agent/password
    @POST("api/agent/password")
    Call<ApiResponse> changePasswordAgent(@Header("token") String token, @Body ChangePassword changePassword);

    //{{base}}/api/user/order/updateOrder
    @POST("api/user/order/updateOrder")
    Call<ApiResponse> userCancelOrder(@Header("token") String token, @Body CancelOrder order);

    //{{base}}/api/user/resetPassword
    @POST("/api/user/resetPassword")
    Call<ApiResponse> userSetResetPassword(@Body ResetPassword resetPassword);

    //{{base}}/api/user/forgetPassword
    @POST("api/user/forgetPassword")
    Call<ApiResponse> userForgetPassword(@Query("email") String email);

    //{{base}}/api/agent/resetPassword
    @POST("/api/agent/resetPassword")
    Call<ApiResponse> agentSetResetPassword(@Body ResetPassword resetPassword);

    //{{base}}/api/agent/forgetPassword
    @POST("api/agent/forgetPassword")
    Call<ApiResponse> agentForgetPassword(@Query("email") String email);

    //{{base}}/api/user/order/rate
    @POST("api/user/order/rate")
    Call<ApiResponse> userRateService(@Header("token") String token, @Body RateService rateService);

    //{{base}}/api/user/offers
    @GET("api/user/offers")
    Call<ApiResponse> sales();

    //{{base}}/api/user/offer_detail?id=2
    @GET("api/user/offer_detail")
    Call<ApiResponse> salesDetails(@Query("id") int id);

    //{{base}}/api/agent/order/updateOrder
    @POST("api/agent/order/updateOrder")
    Call<ApiResponse> agentChangeStatus(@Header("token") String token, @Body Order order);

    //{{base}}/api/agent/order/qr_code
    @POST("api/agent/order/qr_code")
    Call<ApiResponse> qrCode(@Header("token") String token, @Body Order order);

    //{{base}}/api/user/order/scan_code
    @POST("api/user/order/scan_code")
    Call<ApiResponse> getResultQr(@Header("token") String token, @Body Order order);

    //{{base}}/api/user/addRate
    @POST("api/user/addRate")
    Call<ApiResponse> rateApp(@Header("token") String token, @Body RateApp rateApp);

    //{{base}}/api/settings
    @GET("api/settings")
    Call<ApiResponse> support();

    //{{base}}/api/agent/order/statistics
    @GET("api/agent/order/statistics")
    Call<ApiResponse> orderStatistics(@Header("token") String token);

    //{{base}}/api/user/setToken
    @POST("api/user/setToken")
    Call<ApiResponse> UserSetToken(@Header("token") String token, @Body SetToken setToken);

    //{{base}}/api/agent/setToken
    @POST("api/agent/setToken")
    Call<ApiResponse> agentSetToken(@Header("token") String token, @Body SetToken setToken);

    //{{base}}/api/agent/updateImage
    @Multipart
    @POST("api/agent/updateImage")
    Call<ApiResponse> agentupdateImage(@Header("token") String token,
                                       @Part MultipartBody.Part image);

    //{{base}}/api/user/updateImage
    @Multipart
    @POST("api/user/updateImage")
    Call<ApiResponse> userupdateImage(@Header("token") String token,
                                      @Part MultipartBody.Part image);

    //{{base}}/api/user/agentRate
    @POST("api/user/agentRate")
    Call<ApiResponse> rateWorker(@Header("token") String token, @Body RateService rateService);

    //{{base}}/api/user/order/checkOffer
    @FormUrlEncoded
    @POST("api/user/order/checkOffer")
    Call<ApiResponse> checkCopoun(@Field("coupon") String coupon);

    //{{base}}/api/agent/available
    @GET("api/agent/available")
    Call<ApiResponse> agentAvailable(@Header("token") String token);

    //{{base}}/api/agent/order/cancelRequest
    @POST("api/agent/order/cancelRequest")
    Call<ApiResponse> agentAcceptRefuseRequest(@Header("token") String token, @Body Order order);

    //{{base}}/api/user/reservation/create
    @POST("api/user/reservation/create")
    Call<ApiResponse> createReservation(@Header("token") String token, @Body Order order);

    //https://samoola.dtagdev.com/api/socialLogin
    //String loginAs, String name,String email,String phone, String socialId, String socialType
    @FormUrlEncoded
    @POST("/api/socialLogin")
    Call<ApiResponse> socialLogin(@Field("login_as") String loginAs, @Field("name") String name
            , @Field("email") String email, @Field("phone") String phone,
                                  @Field("social_id") String socialId, @Field("social_type") String socialType);

    //https://samoola.dtagdev.com/api/user/notifications
    @GET("api/user/notifications")
    Call<ApiResponse> getNotifications(@Header("token") String token, @Query("page") int page);

    //https://osta.dtagdev.com/api/user/notifications/view
    @FormUrlEncoded
    @POST("api/user/notifications/view")
    Call<ApiResponse> readNotification(@Header("token") String token, @Field("notification_id") int id);

    //http://samoola.dtagdev.com/api/user/construction/Constructiontype
    @GET("api/user/construction/Constructiontype")
    Call<ApiResponse> getConstructionType();

    //http://samoola.dtagdev.com/api/user/construction/list?status=approved
    @GET("api/user/construction/list")
    Call<ApiResponse> getConstList(@Header("token") String token, @Query("status[]") String status[]);

    //http://127.0.0.1:8000/api/user/construction/show?construction_preview_id=8
    @GET("api/user/construction/show")
    Call<ApiResponse> invoice(@Header("token") String token, @Query("construction_preview_id") int construction_preview_id);

    //http://127.0.0.1:8000/api/user/construction/Constructioncreate
    @Multipart
    @POST("api/user/construction/Constructioncreate")
    Call<ApiResponse> makeConstruction(@Header("token") String token,
                                       @Part("construction_type_id") RequestBody construction_type_id,
                                       @Part("city_id") RequestBody city_id,
                                       @Part("levels_count") RequestBody levels_count,
                                       @Part("units_count") RequestBody units_count,
                                       @Part("address") RequestBody address,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude,
                                       @Part("date") RequestBody date,
                                       @Part("time") RequestBody time,
                                       @Part("description") RequestBody description,
                                       @Part("payment_method") RequestBody payment_method,
                                       @Part("area") RequestBody area,
                                       @Part List<MultipartBody.Part> images,
                                       @Part("points") RequestBody points,
                                       @Part("coupon") RequestBody coupon
    );

    //{{base}}/api/user/construction/updateOrder
    @POST("/api/user/construction/updateOrder")
    Call<ApiResponse> userCancelConstruction(@Header("token") String token, @Body CancelOrder order);

}
