package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QtAccountModel(
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

    @SerializedName("upi")
    @Expose
    val upi: Any? = null,

    @SerializedName("fetch_acc")
    @Expose
    val fetchAcc: Int? = null,

    @SerializedName("bid")
    @Expose
    val bid: Int = 0,

    @SerializedName("bname")
    @Expose
    val bname: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null
)
