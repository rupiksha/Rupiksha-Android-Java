package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ministatement(
    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("txnType")
    @Expose
    val txnType: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("narration")
    @Expose
    val narration: String? = null
)
