package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FetchBillModel(
    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("paramname")
    @Expose
    val paramname: String? = null,

    @SerializedName("baseparam")
    @Expose
    val paramvalue: String? = null,

    @SerializedName("item1")
    @Expose
    val item1: String? = null,

    @SerializedName("item1value")
    @Expose
    val item1value: String? = null,

    @SerializedName("item2")
    @Expose
    val item2: String? = null,

    @SerializedName("item2value")
    @Expose
    val item2value: String? = null,

    @SerializedName("item3")
    @Expose
    val item3: String? = null,

    @SerializedName("item3value")
    @Expose
    val item3value: String? = null,

    @SerializedName("support")
    @Expose
    val support: String? = null,

    @SerializedName("skey")
    @Expose
    val skey: String? = null
)
