package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelSubscriptionPayBill(
    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("paramdetails")
    @Expose
    val paramdetails: List<ModelParamdetail>? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("item1")
    @Expose
    val item1: String? = null,

    @SerializedName("item2")
    @Expose
    val item2: String? = null,

    @SerializedName("amount1")
    @Expose
    val amount1: String? = null,

    @SerializedName("amount2")
    @Expose
    val amount2: Int? = null,

    @SerializedName("total")
    @Expose
    val total: Int? = null,

    @SerializedName("support")
    @Expose
    val support: String? = null
)
