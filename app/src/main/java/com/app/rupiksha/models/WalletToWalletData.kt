package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletToWalletData(
    @SerializedName("date")
    @Expose
    var date: String? = null,

    @SerializedName("senderName")
    @Expose
    var senderName: String? = null,

    @SerializedName("senderMobile")
    @Expose
    var senderMobile: String? = null,

    @SerializedName("receiverName")
    @Expose
    var receiverName: String? = null,

    @SerializedName("receiverMobile")
    @Expose
    var receiverMobile: String? = null,

    @SerializedName("txnid")
    @Expose
    var txnid: String? = null,

    @SerializedName("amount")
    @Expose
    var amount: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("support")
    @Expose
    var support: String? = null
)
