package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletFetchUser(
    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null
)
