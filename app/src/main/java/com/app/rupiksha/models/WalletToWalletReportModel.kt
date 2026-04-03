package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletToWalletReportModel(
    @SerializedName("senderName")
    @Expose
    val senderName: String? = null,

    @SerializedName("senderPhone")
    @Expose
    val senderPhone: String? = null,

    @SerializedName("receiverName")
    @Expose
    val receiverName: String? = null,

    @SerializedName("receiverphone")
    @Expose
    val receiverphone: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: Int,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null
)
