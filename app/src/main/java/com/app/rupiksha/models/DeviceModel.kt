package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeviceModel(
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    
    @SerializedName("name")
    @Expose
    var name: String? = null
)
