package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SupportTicket(
    @SerializedName("id")
    @Expose
    val id: Int,
    
    @SerializedName("uid")
    @Expose
    val uid: String? = null,
    
    @SerializedName("ticketid")
    @Expose
    val ticketid: String? = null,
    
    @SerializedName("service")
    @Expose
    val service: String? = null,
    
    @SerializedName("txnid")
    @Expose
    val txnid: String? = null,
    
    @SerializedName("message")
    @Expose
    val message: String? = null,
    
    @SerializedName("adminmsg")
    @Expose
    val adminmsg: String? = null,
    
    @SerializedName("status")
    @Expose
    val status: String? = null,
    
    @SerializedName("date")
    @Expose
    val date: String? = null
)
