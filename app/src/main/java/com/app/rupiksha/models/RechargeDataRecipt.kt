package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeDataRecipt(
    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("item1")
    @Expose
    val item1: String? = null,

    @SerializedName("item2")
    @Expose
    val item2: String? = null,

    @SerializedName("amount1")
    @Expose
    val amount1: Double = 0.0,

    @SerializedName("amount2")
    @Expose
    val amount2: Double = 0.0,

    @SerializedName("total")
    @Expose
    val total: Double = 0.0,

    @SerializedName("support")
    @Expose
    val support: String? = null
)
