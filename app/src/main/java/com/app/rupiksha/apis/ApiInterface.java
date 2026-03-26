package com.app.rupiksha.apis;

import com.app.rupiksha.models.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //String BASE_URL = "https://demo.ecuzenproducts.in/api/";//Demo Url
//
    String BASE_URL = "https://rupiksha.com/api/";//Live Url

    @GET("getRole")
    Call<BaseResponse> getrole();

    @POST("register")
    Call<BaseResponse> registration(@Body RequestBody requestBody);

    @POST("login")
    Call<BaseResponse> login(@Body RequestBody requestBody);

    @POST("loginVerify")
    Call<BaseResponse> otpVerify(@Body RequestBody requestBody);

    @GET("getrdname")
    Call<BaseResponse> getDeiceList();

    @GET("getbank")
    Call<BaseResponse> getBankList();

    @GET("getAllstate")
    Call<BaseResponse> getStateList();

    @GET("address-proof-list")
    Call<BaseResponse> getDocumentList();

    @POST("forgot/password")
    Call<BaseResponse> forgetpassword(@Body RequestBody requestBody);

    @POST("forgot/pin")
    Call<BaseResponse> forgetpin(@Body RequestBody requestBody);

    @POST("update-user-location")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> checkuserlocation(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @POST("update-profile")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> editProfile(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedPan);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("view-profile")
    Call<BaseResponse> getUserInfo(@HeaderMap Map<String, String> headers);


    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("user-logout")
    Call<BaseResponse> getUserLogout(@HeaderMap Map<String, String> headers);


    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("contact-data")
    Call<BaseResponse> getSupport(@HeaderMap Map<String, String> headers);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("services")
    Call<BaseResponse> getService(@HeaderMap Map<String, String> headers);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("wallet")
    Call<BaseResponse> getWalletBalance(@HeaderMap Map<String, String> headers);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("bbps/all_categories")
    Call<BaseResponse> getbbpsService(@HeaderMap Map<String, String> headers);

    @Multipart
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("submit-kyc")
    Call<BaseResponse> getuserKyc(@HeaderMap HashMap<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedPan, @Part MultipartBody.Part uploadedAadharFront, @Part MultipartBody.Part uploadedAadharBack,@Part MultipartBody.Part uploaddocument,@Part MultipartBody.Part uploadselfie);

    @POST("update-password")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> changePassword(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("update-pin")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> changePin(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("bbps/billers")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getBillList(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("bbps/fetch_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getFetchBill(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("bbps/pay_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getPayBill(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("topup")
    Call<BaseResponse> getAddMoney(@HeaderMap HashMap<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedPan);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("addfund-banklist")
    Call<BaseResponse> getfundbanklist(@HeaderMap Map<String, String> headers);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("aeps/checkKyc")
    Call<BaseResponse> getCheckKyc(@HeaderMap Map<String, String> headers);

    @Multipart
    @POST("aeps/dokyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getDokyc(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedshopImage);


    @Multipart
    @POST("aeps/updatedokyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getUpdateAepsKyc(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedshopImage);


    @POST("aeps/verify-otp")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> verifyAepsOtp(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("aeps/send-otp")
    Call<BaseResponse> getAepsOtp(@HeaderMap Map<String, String> headers, @Query("lat") String latitude, @Query("log") String longitude);

    @POST("aeps/verify-finger")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> verifyFingurePrint(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("aeps/tfa-verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> verifyTwoFAFingurePrint(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("aeps/ap-tfa-verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> verifyAPTwoFAFingurePrint(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("aeps/balance-enquiry")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> aepsBalanceEnquries(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("aeps/cash-withdrawal")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> aepsCashWithdrawl(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("aeps/mini-statement")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> aepsministatement(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @POST("aeps/aadhar-pay")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> aepsaadharPay(@HeaderMap Map<String, String> headers, @PartMap() Map<String, RequestBody> partMa);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("recharge/get-operator/mobile")
    Call<BaseResponse> getOperator(@HeaderMap Map<String, String> headers);

    @POST("recharge/fetch-operator")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> fetchoperator(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("recharge/do-recharge")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> doRecharge(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("qtransfer/fetch-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> fetchaccount(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @GET("qtransfer/getAllBankIfsc")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getbankifsc(@HeaderMap Map<String, String> headers);

    @POST("qtransfer/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> initiateqttransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("qtransfer/qt-do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> doqttransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @GET("payout/get-accounts")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getPayoutBank(@HeaderMap Map<String, String> headers);

    @Multipart
    @POST("payout/add-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> addPayoutAccount(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedPan);

    @POST("payout/delete-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> deletePayoutAccount(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("payout/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse>  initiatePayoutTransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("payout/do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> dopayouttransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @GET("get-all-report-type")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getAllReportType(@HeaderMap Map<String, String> headers);

    @POST("get-report")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getReport(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @GET("get-commission-plans")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getCommissionPlan(@HeaderMap Map<String, String> headers);

    @POST("wallet-to-wallet/fetch-by-number")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getFetchUser(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("wallet-to-wallet/do-transactions")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getdowalletTransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/login")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getremitterLogin(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/kyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getDMTKyc(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/register")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getremitterRegister(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/data")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getdmtaccounttype(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/add-bank-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> addDmtAccount(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("dmt/bank-list")
    Call<BaseResponse> getDmtBank(@HeaderMap Map<String, String> headers);

    @POST("dmt/delete-bank-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> deleteDmtAccount(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> initiateDMTTransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> doDMTTransaction(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/logout")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> logoutDmt(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @POST("uti/register")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getPSARegistration(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("uti/check-status")
    Call<BaseResponse> getUtiStatus(@HeaderMap Map<String, String> headers);

    @POST("uti/buy-coupon")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> doBuyCoupon(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("support/create-ticket")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> doRaiseComplained(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("support/supprt-type")
    Call<BaseResponse> getSupportType(@HeaderMap Map<String, String> headers);

    @POST("qtransfer/accountVerify")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> qtaccountverify(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("other-service/pan_verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getpanverication(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("subscription/{FURL}")
    Call<BaseResponse> getBillers(@Path("FURL") String type, @HeaderMap Map<String, String> headers);

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("subscription/{FURL}/{MURL}/{GURL}")
    Call<BaseResponse> getBillersDetails(@Path("FURL") String type, @Path("MURL") Integer id, @Path("GURL") String name, @HeaderMap Map<String, String> headers);

    @POST("subscription/fetch_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getsubscriptionfetchbill(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("subscription/pay_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getsubscriptionpaybill(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @Multipart
    @POST("file-uploads")
    Call<BaseResponse> uploadImage(@PartMap() Map<String, RequestBody> partMa,
                                   @HeaderMap HashMap<String, String> header,
                                   @Part MultipartBody.Part uploadedFiler);

    @Multipart
    @POST("itr-forms")
    Call<BaseResponse> submitItrForm(@PartMap() Map<String, RequestBody> partMa,
                                     @HeaderMap HashMap<String, String> header);


    @POST("dmt/validate/aadhar")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getVAadharApi(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("dmt/validate/otp")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> getValidateOtpApi(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("payout/account-verify")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> payoutAccountVerify(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);

    @POST("cms_url")
    @Headers("X-Requested-With: XMLHttpRequest")
    Call<BaseResponse> cmsServiceapi(@HeaderMap Map<String, String> headers, @Body RequestBody requestBody);
}

