package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AepsServiceModel(
    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("icon")
    @Expose
    val icon: String? = null
)
