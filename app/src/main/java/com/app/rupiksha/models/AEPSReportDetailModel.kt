package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AEPSReportDetailModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("txnamount")
    @Expose
    val txnamount: Any? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("aadhar")
    @Expose
    val aadhar: String? = null,

    @SerializedName("rrn")
    @Expose
    val rrn: String? = null,

    @SerializedName("bank")
    @Expose
    val bank: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("response")
    @Expose
    val response: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("tempid")
    @Expose
    val tempid: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

    @SerializedName("bname")
    @Expose
    val bname: String? = null,

    @SerializedName("mode")
    @Expose
    val mode: String? = null,

    @SerializedName("utr")
    @Expose
    val utr: String? = null,

    @SerializedName("req")
    @Expose
    val req: String? = null,

    @SerializedName("canumber")
    @Expose
    val canumber: String? = null,

    @SerializedName("boperator")
    @Expose
    val boperator: String? = null,

    @SerializedName("displayName")
    @Expose
    val displayName: String? = null,

    @SerializedName("qty")
    @Expose
    val qty: String? = null,

    @SerializedName("txn_uid")
    @Expose
    val txnUid: Any? = null,

    @SerializedName("txntype")
    @Expose
    val txntype: String? = null,

    @SerializedName("remark")
    @Expose
    val remark: String? = null,

    @SerializedName("opening")
    @Expose
    val opening: String? = null,

    @SerializedName("closing")
    @Expose
    val closing: String? = null,

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

    var progressDelay: Boolean = false
)
