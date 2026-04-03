package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressProof(
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
)
