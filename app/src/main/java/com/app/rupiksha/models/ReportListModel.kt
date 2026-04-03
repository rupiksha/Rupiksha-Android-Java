package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportListModel(
    @SerializedName("ad_id")
    @Expose
    val adId: Int,

    @SerializedName("banner")
    @Expose
    val banner: String? = null
)
