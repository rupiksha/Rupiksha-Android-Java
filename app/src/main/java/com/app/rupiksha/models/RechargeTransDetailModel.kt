package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeTransDetailModel(
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,

    @SerializedName("operatorName")
    @Expose
    val operatorName: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null
)
