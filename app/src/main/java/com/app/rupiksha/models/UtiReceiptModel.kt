package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UtiReceiptModel(
    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

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
    val amount1: Int = 0,

    @SerializedName("amount2")
    @Expose
    val amount2: Int = 0,

    @SerializedName("support")
    @Expose
    val support: String? = null
)
