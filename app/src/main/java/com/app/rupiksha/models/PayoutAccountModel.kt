package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PayoutAccountModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("uid")
    @Expose
    val uid: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

    @SerializedName("passbook")
    @Expose
    val passbook: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null
)
