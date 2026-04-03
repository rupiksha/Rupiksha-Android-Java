package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FaqList(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("package")
    @Expose
    val _package: String? = null,

    @SerializedName("amount")
    @Expose
    val amount: String? = null,

    @SerializedName("percent")
    @Expose
    val percent: String? = null,

    @SerializedName("froma")
    @Expose
    val froma: String? = null,

    @SerializedName("toa")
    @Expose
    val toa: String? = null,

    @SerializedName("commission")
    @Expose
    val commission: String? = null,

    @SerializedName("operator")
    @Expose
    val operator: String? = null,

    @SerializedName("opid")
    @Expose
    val opid: String? = null
)
