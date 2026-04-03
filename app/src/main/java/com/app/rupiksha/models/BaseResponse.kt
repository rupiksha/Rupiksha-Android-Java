package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status")
    @Expose
    val status: String? = null,
    
    @SerializedName("error")
    @Expose
    val error: Boolean = false,
    
    @SerializedName("message")
    @Expose
    val message: String? = null,
    
    @SerializedName("mode")
    @Expose
    val mode: String? = null,
    
    @SerializedName("log_key")
    @Expose
    val logKey: String? = null,
    
    @SerializedName("data")
    @Expose
    val data: MainData? = null,
    
    @SerializedName("header_token")
    @Expose
    val headerToken: String? = null,
    
    @SerializedName("header_key")
    @Expose
    val headerKey: String? = null,
    
    @SerializedName("news")
    @Expose
    val news: String? = null,
    
    @SerializedName("walletBalance")
    @Expose
    val walletBalance: Double = 0.0,
    
    @SerializedName("skey")
    @Expose
    val skey: String? = null,
    
    @SerializedName("aepsStatus")
    @Expose
    val aepsStatus: String? = null,
    
    @SerializedName("tfa")
    @Expose
    val tfa: Boolean = false,
    
    @SerializedName("aptfa")
    @Expose
    val aptfa: Boolean = false,
    
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    
    @SerializedName("operator")
    @Expose
    val operator: String? = null,
    
    @SerializedName("circle")
    @Expose
    val circle: String? = null,
    
    @SerializedName("txn_key")
    @Expose
    val txnKey: String? = null,
    
    @SerializedName("dmtKey")
    @Expose
    val dmtKey: String? = null,
    
    @SerializedName("activity")
    @Expose
    val activity: String? = null,
    
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
    
    @SerializedName("name")
    @Expose
    val name: String? = null,
    
    @SerializedName("remainingLimit")
    @Expose
    val remainingLimit: String? = null,
    
    @SerializedName("utiStatus")
    @Expose
    val utiStatus: String? = null,
    
    @SerializedName("psaid")
    @Expose
    val psaid: String? = null,
    
    @SerializedName("couponCharge")
    @Expose
    val couponCharge: Double = 0.0,
    
    @SerializedName("total_record")
    @Expose
    val totalRecord: Int = 0,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("key")
    @Expose
    val key: Int? = null,

    @SerializedName("msg")
    @Expose
    val msg: String? = null,

    @SerializedName("url")
    @Expose
    val url: String? = null,

    @SerializedName("otpid")
    @Expose
    val otpid: String? = null,

    @SerializedName("aadhar")
    @Expose
    val aadhar: String? = null
)
