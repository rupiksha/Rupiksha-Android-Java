package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlanModel(
    @SerializedName("allPlans")
    @Expose
    val allPlans: AllPlans? = null,

    @SerializedName("planTitle")
    @Expose
    val planTitle: List<PlanDataModel>? = null
)
