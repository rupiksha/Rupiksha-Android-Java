package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GlobalBankModel(
    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("ifscGlobal")
    @Expose
    val ifscGlobal: String? = null
)
