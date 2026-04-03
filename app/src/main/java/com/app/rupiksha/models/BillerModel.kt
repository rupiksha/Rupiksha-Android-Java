package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BillerModel(
    @SerializedName("id")
    @Expose
    val id: Int,
    
    @SerializedName("name")
    @Expose
    val name: String? = null,
    
    @SerializedName("category")
    @Expose
    val category: String? = null,
    
    @SerializedName("viewbill")
    @Expose
    val viewbill: String? = null,
    
    @SerializedName("regex")
    @Expose
    val regex: String? = null,
    
    @SerializedName("displayname")
    @Expose
    val displayname: String? = null,
    
    @SerializedName("ad1_d_name")
    @Expose
    val ad1DName: String? = null,
    
    @SerializedName("ad1_name")
    @Expose
    val ad1Name: String? = null,
    
    @SerializedName("ad1_regex")
    @Expose
    val ad1Regex: String? = null,
    
    @SerializedName("ad2_d_name")
    @Expose
    val ad2DName: String? = null,
    
    @SerializedName("ad2_name")
    @Expose
    val ad2Name: String? = null,
    
    @SerializedName("ad2_regex")
    @Expose
    val ad2Regex: String? = null,
    
    @SerializedName("ad3_d_name")
    @Expose
    val ad3DName: String? = null,
    
    @SerializedName("ad3_name")
    @Expose
    val ad3Name: String? = null,
    
    @SerializedName("ad3_regex")
    @Expose
    val ad3Regex: String? = null,
    
    @SerializedName("catkey")
    @Expose
    val catkey: String? = null,
    
    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("param1")
    @Expose
    val param1: String? = null,
    
    @SerializedName("param1_name")
    @Expose
    val param1_name: String? = null,
    
    @SerializedName("param1_type")
    @Expose
    val param1_type: String? = null,
    
    @SerializedName("param1_regex")
    @Expose
    val param1_regex: String? = null,

    @SerializedName("param2")
    @Expose
    val param2: String? = null,
    
    @SerializedName("param2_name")
    @Expose
    val param2_name: String? = null,
    
    @SerializedName("param2_type")
    @Expose
    val param2_type: String? = null,
    
    @SerializedName("param2_regex")
    @Expose
    val param2_regex: String? = null,

    @SerializedName("operator_id")
    @Expose
    val operator_id: String? = null,
    
    @SerializedName("fetchBill")
    @Expose
    val fetchBill: String? = null
)
