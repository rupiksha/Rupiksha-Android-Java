package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KycModel(
    @SerializedName("address")
    @Expose
    val address: String? = null,
    
    @SerializedName("pincode")
    @Expose
    val pincode: String? = null,
    
    @SerializedName("adhaar")
    @Expose
    val adhaar: String? = null,
    
    @SerializedName("pan")
    @Expose
    val pan: String? = null,
    
    @SerializedName("adhaarimg")
    @Expose
    val adhaarimg: String? = null,
    
    @SerializedName("panimg")
    @Expose
    val panimg: String? = null,
    
    @SerializedName("shopname")
    @Expose
    val shopname: String? = null,
    
    @SerializedName("shopaddress")
    @Expose
    val shopaddress: String? = null,
    
    @SerializedName("active")
    @Expose
    val active: String? = null,
    
    @SerializedName("state")
    @Expose
    val state: String? = null,
    
    @SerializedName("district")
    @Expose
    val district: String? = null,
    
    @SerializedName("adhaarback")
    @Expose
    val adhaarback: String? = null,
    
    @SerializedName("fname")
    @Expose
    val fname: String? = null,
    
    @SerializedName("date")
    @Expose
    val date: String? = null,
    
    @SerializedName("dob")
    @Expose
    val dob: String? = null
)
