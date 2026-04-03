package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BbpsFetchBillModel(
    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: Int = 0,

    @SerializedName("canumber")
    @Expose
    val canumber: String? = null,

    @SerializedName("boperator")
    @Expose
    val boperator: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("operatorName")
    @Expose
    val operatorName: String? = null,

    @SerializedName("displayname")
    @Expose
    val displayname: String? = null
)
