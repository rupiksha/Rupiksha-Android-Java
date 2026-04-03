package com.app.rupiksha.models

import com.google.gson.annotations.SerializedName

data class BbpsReportModel(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("uid")
    val uid: String? = null,
    @SerializedName("txnid")
    val txnid: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("amount")
    val amount: String? = null,
    @SerializedName("req")
    val req: String? = null,
    @SerializedName("response")
    val response: String? = null,
    @SerializedName("canumber")
    val canumber: String? = null,
    @SerializedName("boperator")
    val boperator: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("displayName")
    val displayName: String? = null
)
