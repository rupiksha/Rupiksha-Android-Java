package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FaqModel(
    @SerializedName("label")
    @Expose
    val label: String? = null,

    @SerializedName("tableData")
    @Expose
    val tableData: List<FaqList>? = null
)
