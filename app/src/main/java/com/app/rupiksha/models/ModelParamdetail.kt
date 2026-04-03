package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelParamdetail(
    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("value")
    @Expose
    val value: String? = null
)
