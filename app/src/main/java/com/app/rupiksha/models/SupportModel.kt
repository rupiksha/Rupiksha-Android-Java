package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SupportModel(
    @SerializedName("title")
    @Expose
    val title: String? = null,
    
    @SerializedName("logo")
    @Expose
    val logo: String? = null,
    
    @SerializedName("cnumber")
    @Expose
    val cnumber: String? = null,
    
    @SerializedName("cemail")
    @Expose
    val cemail: String? = null,
    
    @SerializedName("news")
    @Expose
    val news: String? = null,
    
    @SerializedName("address")
    @Expose
    val address: String? = null,
    
    @SerializedName("facebook")
    @Expose
    val facebook: String? = null,
    
    @SerializedName("youtube")
    @Expose
    val youtube: String? = null,
    
    @SerializedName("twitter")
    @Expose
    val twitter: String? = null,
    
    @SerializedName("instagram")
    @Expose
    val instagram: String? = null,
    
    @SerializedName("linkedin")
    @Expose
    val linkedin: String? = null
)