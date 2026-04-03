package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AepsReceiptData(
    @SerializedName("status")
    @Expose
    val status: String? = null,
    
    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,
    
    @SerializedName("rrn")
    @Expose
    val rrn: String? = null,
    
    @SerializedName("bank")
    @Expose
    val bank: String? = null,
    
    @SerializedName("aadhar")
    @Expose
    val aadhar: String? = null,
    
    @SerializedName("date")
    @Expose
    val date: String? = null,
    
    @SerializedName("amount")
    @Expose
    val amount: String? = null,
    
    @SerializedName("txnamount")
    @Expose
    val txnamount: String? = null,
    
    @SerializedName("balance")
    @Expose
    val balance: String? = null
)
