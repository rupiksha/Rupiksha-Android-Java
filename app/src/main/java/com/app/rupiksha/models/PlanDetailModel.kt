package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanDetailModel(
    @SerializedName("price")
    @Expose
    val rs: Int,

    @SerializedName("validity")
    @Expose
    val validity: String? = null,

    @SerializedName("desc")
    @Expose
    val desc: String? = null,

    @SerializedName("Type")
    @Expose
    val type: String? = null,

    @SerializedName("data")
    @Expose
    val data: String? = null,

    @SerializedName("sms")
    @Expose
    val sms: String? = null
) : Serializable
