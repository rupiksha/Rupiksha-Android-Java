package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BankListModel(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("catkey")
    @Expose
    val catkey: String? = null,

    @SerializedName("category")
    @Expose
    val category: String? = null,

    @SerializedName("viewbill")
    @Expose
    val viewbill: String? = null,

    @SerializedName("displayname")
    @Expose
    val displayname: String? = null,

    @SerializedName("ad1dname")
    @Expose
    val ad1dname: Any? = null,

    @SerializedName("ad1name")
    @Expose
    val ad1name: Any? = null,

    @SerializedName("ad2dname")
    @Expose
    val ad2dname: Any? = null,

    @SerializedName("ad2name")
    @Expose
    val ad2name: Any? = null,

    @SerializedName("ad3dname")
    @Expose
    val ad3dname: Any? = null,

    @SerializedName("ad3name")
    @Expose
    val ad3name: Any? = null
)
