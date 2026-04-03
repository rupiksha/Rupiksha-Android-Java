package com.app.rupiksha.apis

import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    companion object {
        const val BASE_URL = "https://rupiksha.com/api/" // Live Url
    }

    @GET("getRole")
    suspend fun getrole(): Response<BaseResponse>

    @POST("register")
    suspend fun registration(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("login")
    suspend fun login(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("loginVerify")
    suspend fun otpVerify(@Body requestBody: RequestBody): Response<BaseResponse>

    @GET("getrdname")
    suspend fun getDeiceList(): Response<BaseResponse>

    @GET("getbank")
    suspend fun getBankList(): Response<BaseResponse>

    @GET("getAllstate")
    suspend fun getStateList(): Response<BaseResponse>

    @GET("address-proof-list")
    suspend fun getDocumentList(): Response<BaseResponse>

    @POST("forgot/password")
    suspend fun forgetpassword(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("forgot/pin")
    suspend fun forgetpin(@Body requestBody: RequestBody): Response<BaseResponse>

    @POST("update-user-location")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun checkuserlocation(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Multipart
    @POST("update-profile")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun editProfile(
        @HeaderMap headers: Map<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedPan: MultipartBody.Part
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("view-profile")
    suspend fun getUserInfo(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("user-logout")
    suspend fun getUserLogout(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("contact-data")
    suspend fun getSupport(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("services")
    suspend fun getService(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("wallet")
    suspend fun getWalletBalance(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("bbps/all_categories")
    suspend fun getbbpsService(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Multipart
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("submit-kyc")
    suspend fun getuserKyc(
        @HeaderMap headers: HashMap<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedPan: MultipartBody.Part,
        @Part uploadedAadharFront: MultipartBody.Part,
        @Part uploadedAadharBack: MultipartBody.Part,
        @Part uploaddocument: MultipartBody.Part,
        @Part uploadselfie: MultipartBody.Part
    ): Response<BaseResponse>

    @POST("update-password")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun changePassword(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("update-pin")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun changePin(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("bbps/billers")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getBillList(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("bbps/fetch_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getFetchBill(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("bbps/pay_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getPayBill(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Multipart
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("topup")
    suspend fun getAddMoney(
        @HeaderMap headers: HashMap<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedPan: MultipartBody.Part
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("addfund-banklist")
    suspend fun getfundbanklist(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("aeps/checkKyc")
    suspend fun getCheckKyc(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Multipart
    @POST("aeps/dokyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getDokyc(
        @HeaderMap headers: Map<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedshopImage: MultipartBody.Part
    ): Response<BaseResponse>

    @Multipart
    @POST("aeps/updatedokyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getUpdateAepsKyc(
        @HeaderMap headers: Map<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedshopImage: MultipartBody.Part
    ): Response<BaseResponse>

    @POST("aeps/verify-otp")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun verifyAepsOtp(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("aeps/send-otp")
    suspend fun getAepsOtp(
        @HeaderMap headers: Map<String, String>,
        @Query("lat") latitude: String,
        @Query("log") longitude: String
    ): Response<BaseResponse>

    @POST("aeps/verify-finger")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun verifyFingurePrint(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("aeps/tfa-verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun verifyTwoFAFingurePrint(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("aeps/ap-tfa-verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun verifyAPTwoFAFingurePrint(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("aeps/balance-enquiry")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun aepsBalanceEnquries(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("aeps/cash-withdrawal")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun aepsCashWithdrawl(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("aeps/mini-statement")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun aepsministatement(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Multipart
    @POST("aeps/aadhar-pay")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun aepsaadharPay(
        @HeaderMap headers: Map<String, String>,
        @PartMap partMa: Map<String, RequestBody>
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("recharge/get-operator/{type}")
    suspend fun getOperator(
        @Path("type") type: String,
        @HeaderMap headers: Map<String, String>
    ): Response<BaseResponse>

    @POST("recharge/fetch-operator")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun fetchoperator(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("recharge/do-recharge")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun doRecharge(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("qtransfer/fetch-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun fetchaccount(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("qtransfer/getAllBankIfsc")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getbankifsc(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("qtransfer/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun initiateqttransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("qtransfer/qt-do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun doqttransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("payout/get-accounts")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getPayoutBank(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @Multipart
    @POST("payout/add-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun addPayoutAccount(
        @HeaderMap headers: Map<String, String>,
        @PartMap map: HashMap<String, RequestBody>,
        @Part uploadedPan: MultipartBody.Part
    ): Response<BaseResponse>

    @POST("payout/delete-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun deletePayoutAccount(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("payout/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun initiatePayoutTransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("payout/do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun dopayouttransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("get-all-report-type")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getAllReportType(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("get-report")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getReport(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @GET("get-commission-plans")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getCommissionPlan(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("wallet-to-wallet/fetch-by-number")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getFetchUser(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("wallet-to-wallet/do-transactions")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getdowalletTransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/login")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getremitterLogin(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/kyc")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getDMTKyc(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/register")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getremitterRegister(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/data")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getdmtaccounttype(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/add-bank-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun addDmtAccount(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("dmt/bank-list")
    suspend fun getDmtBank(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("dmt/delete-bank-account")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun deleteDmtAccount(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/initiate-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun initiateDMTTransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/do-transaction")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun doDMTTransaction(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/logout")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun logoutDmt(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Multipart
    @POST("uti/register")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getPSARegistration(
        @HeaderMap headers: Map<String, String>,
        @PartMap map: HashMap<String, RequestBody>
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("uti/check-status")
    suspend fun getUtiStatus(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("uti/buy-coupon")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun doBuyCoupon(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("support/create-ticket")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun doRaiseComplained(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("support/supprt-type")
    suspend fun getSupportType(@HeaderMap headers: Map<String, String>): Response<BaseResponse>

    @POST("qtransfer/accountVerify")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun qtaccountverify(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("other-service/pan_verification")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getpanverication(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("subscription/{FURL}")
    suspend fun getBillers(
        @Path("FURL") type: String,
        @HeaderMap headers: Map<String, String>
    ): Response<BaseResponse>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("subscription/{FURL}/{MURL}/{GURL}")
    suspend fun getBillersDetails(
        @Path("FURL") type: String,
        @Path("MURL") id: Int,
        @Path("GURL") name: String,
        @HeaderMap headers: Map<String, String>
    ): Response<BaseResponse>

    @POST("subscription/fetch_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getsubscriptionfetchbill(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("subscription/pay_bill")
    @Headers("X-Requested-With: XMLHttpRequest")
    fun getsubscriptionpaybill(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @Multipart
    @POST("file-uploads")
    suspend fun uploadImage(
        @PartMap partMa: Map<String, RequestBody>,
        @HeaderMap header: HashMap<String, String>,
        @Part uploadedFiler: MultipartBody.Part
    ): Response<BaseResponse>

    @Multipart
    @POST("itr-forms")
    suspend fun submitItrForm(
        @PartMap partMa: Map<String, RequestBody>,
        @HeaderMap header: HashMap<String, String>
    ): Response<BaseResponse>

    @POST("dmt/validate/aadhar")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getVAadharApi(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("dmt/validate/otp")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun getValidateOtpApi(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("payout/account-verify")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun payoutAccountVerify(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>

    @POST("cms_url")
    @Headers("X-Requested-With: XMLHttpRequest")
    suspend fun cmsServiceapi(
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Response<BaseResponse>
}
