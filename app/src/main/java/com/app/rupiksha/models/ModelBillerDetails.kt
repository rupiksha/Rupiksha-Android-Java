package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelBillerDetails(
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("b_id")
    @Expose
    val bId: Int? = null,

    @SerializedName("billerId")
    @Expose
    val billerId: String? = null,

    @SerializedName("biller_name")
    @Expose
    val billerName: String? = null,

    @SerializedName("category_key")
    @Expose
    val categoryKey: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("category_name")
    @Expose
    val categoryName: String? = null,

    @SerializedName("coverage_city")
    @Expose
    val coverageCity: String? = null,

    @SerializedName("coverage_state")
    @Expose
    val coverageState: String? = null,

    @SerializedName("coverage_pincode")
    @Expose
    val coveragePincode: Int? = null,

    @SerializedName("biller_status")
    @Expose
    val billerStatus: String? = null,

    @SerializedName("is_available")
    @Expose
    val isAvailable: Int? = null,

    @SerializedName("fetch_bill")
    @Expose
    val fetchBill: Int? = null,

    @SerializedName("icon_url")
    @Expose
    val iconUrl: String? = null,

    @SerializedName("param1")
    @Expose
    val param1: String? = null,

    @SerializedName("param1_mandatory")
    @Expose
    val param1Mandatory: Int? = null,

    @SerializedName("param1_regex")
    @Expose
    val param1Regex: String? = null,

    @SerializedName("param2")
    @Expose
    val param2: String? = null,

    @SerializedName("param2_mandatory")
    @Expose
    val param2Mandatory: Int? = null,

    @SerializedName("param2_regex")
    @Expose
    val param2Regex: String? = null,

    @SerializedName("param3")
    @Expose
    val param3: String? = null,

    @SerializedName("param3_mandatory")
    @Expose
    val param3Mandatory: Int? = null,

    @SerializedName("param3_regex")
    @Expose
    val param3Regex: String? = null,

    @SerializedName("param4")
    @Expose
    val param4: String? = null,

    @SerializedName("param4_mandatory")
    @Expose
    val param4Mandatory: Int? = null,

    @SerializedName("param4_regex")
    @Expose
    val param4Regex: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null
)
