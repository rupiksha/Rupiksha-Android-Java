package com.app.rupiksha.models

import com.google.gson.annotations.SerializedName

data class ResponseUserInfo(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val userInfo: ModelUserInfo? = null
)
