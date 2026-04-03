package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UtiSupportDataModel(
    @SerializedName("supportmail")
    @Expose
    val supportmail: String? = null,

    @SerializedName("supportphone")
    @Expose
    val supportphone: String? = null
)
