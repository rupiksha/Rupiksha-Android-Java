package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletReportModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("txn_uid")
    @Expose
    val txnUid: Any? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("txntype")
    @Expose
    val txntype: String? = null,

    @SerializedName("remark")
    @Expose
    val remark: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("opening")
    @Expose
    val opening: String? = null,

    @SerializedName("closing")
    @Expose
    val closing: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null
)
