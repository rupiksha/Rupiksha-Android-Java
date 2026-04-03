package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DMTBankdetailListModel(
    @SerializedName("bank_recipient_id")
    @Expose
    var beneId: String? = null,

    @SerializedName("bankid")
    @Expose
    var bankid: String? = null,

    @SerializedName("bank")
    @Expose
    var bankname: String? = null,

    @SerializedName("recipient_name")
    @Expose
    var name: String? = null,

    @SerializedName("recipient_mobile")
    @Expose
    var recipient_mobile: String? = null,

    @SerializedName("account")
    @Expose
    var accno: String? = null,

    @SerializedName("ifsc")
    @Expose
    var ifsc: String? = null,

    @SerializedName("verified")
    @Expose
    var verified: String? = null,

    @SerializedName("banktype")
    @Expose
    var banktype: String? = null,

    @SerializedName("paytm")
    @Expose
    var paytm: Boolean? = null
)
