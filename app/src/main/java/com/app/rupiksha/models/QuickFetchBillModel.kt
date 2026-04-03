package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuickFetchBillModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

    @SerializedName("bname")
    @Expose
    val bname: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("mode")
    @Expose
    val mode: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("response")
    @Expose
    val response: String? = null,

    @SerializedName("rrn")
    @Expose
    val rrn: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null
)
