package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DmtDataModel(
    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("rrn")
    @Expose
    val rrn: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

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
