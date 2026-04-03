package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DmtProfileDataModel(
    @SerializedName("total_monthly_limit")
    @Expose
    val remainingLimit: Int,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null
)
