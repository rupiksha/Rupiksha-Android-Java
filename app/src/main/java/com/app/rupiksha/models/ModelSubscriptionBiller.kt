package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelSubscriptionBiller(
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("billerId")
    @Expose
    val billerId: String? = null,

    @SerializedName("biller")
    @Expose
    val biller: String? = null,

    @SerializedName("icon")
    @Expose
    val icon: String? = null
)
