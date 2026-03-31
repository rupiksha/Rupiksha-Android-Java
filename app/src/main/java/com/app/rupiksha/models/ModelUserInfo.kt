package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelUserInfo(
    @SerializedName("username")
    @Expose
    val username: String? = null,
    
    @SerializedName("name")
    @Expose
    val name: String? = null,
    
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
    
    @SerializedName("email")
    @Expose
    val email: String? = null,
    
    @SerializedName("profile")
    @Expose
    val profile: String? = null,

    @SerializedName("itr_register")
    @Expose
    val itrRegister: String? = null,
    
    @SerializedName("itr_login")
    @Expose
    val itrLogin: String? = null,

    @SerializedName("outlet")
    @Expose
    val outlet: String? = null
)
