package com.app.rupiksha.models

import android.net.Uri

data class BankDetailModel(
    var id: Int,
    var bankName: String? = null,
    var accountNumber: String? = null,
    var imageUri: Uri? = null,
    var imageName: String? = null,
    var bankIfscCode: String? = null,
    var bankType: String? = null,
    var isOtherDocument: Boolean = false
)
