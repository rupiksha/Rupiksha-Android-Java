package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanDataModel(
    @SerializedName("opCatName")
    @Expose
    val opCatName: String? = null,

    @SerializedName("opCatId")
    @Expose
    val opCatId: Int
) : Serializable
