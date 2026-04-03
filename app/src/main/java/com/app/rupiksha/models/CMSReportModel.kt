package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CMSReportModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("operator_name")
    @Expose
    val operatorName: String? = null,

    @SerializedName("req")
    @Expose
    val req: String? = null,

    @SerializedName("res")
    @Expose
    val res: String? = null,

    @SerializedName("debitreq")
    @Expose
    val debitreq: Any? = null,

    @SerializedName("callback")
    @Expose
    val callback: Any? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    var progressDelay: Boolean = false
)
