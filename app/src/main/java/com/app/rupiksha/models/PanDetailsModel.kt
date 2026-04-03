package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PanDetailsModel(
    @SerializedName("name")
    @Expose
    val name: String? = null,
    
    @SerializedName("pan_status")
    @Expose
    val panStatus: String? = null,
    
    @SerializedName("gender")
    @Expose
    val gender: String? = null,
    
    @SerializedName("dob")
    @Expose
    val dob: String? = null,
    
    @SerializedName("constitution")
    @Expose
    val constitution: String? = null
)
