package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UtiReportModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("qty")
    @Expose
    val qty: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("response")
    @Expose
    val response: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null
)
