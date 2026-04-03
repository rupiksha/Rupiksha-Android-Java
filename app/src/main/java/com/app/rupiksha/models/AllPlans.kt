package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllPlans(
    @SerializedName("catId")
    @Expose
    val catId: Int,

    @SerializedName("catArray")
    @Expose
    val catArray: List<PlanDetailModel>? = null
) : Serializable
