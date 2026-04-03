package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportTypeModel(
    @SerializedName("id")
    @Expose
    val id: Int,
    
    @SerializedName("name")
    @Expose
    val name: String? = null,
    
    @SerializedName("type")
    @Expose
    val type: String? = null,
    
    @SerializedName("image")
    @Expose
    val image: String? = null
)
