package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddfundBankModel(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("account")
    @Expose
    val account: String? = null,

    @SerializedName("ifsc")
    @Expose
    val ifsc: String? = null,

    @SerializedName("bank")
    @Expose
    val bank: String? = null,

    @SerializedName("bname")
    @Expose
    val bname: String? = null
)
