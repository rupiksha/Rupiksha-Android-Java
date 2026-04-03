package com.app.rupiksha.models

import android.net.Uri

data class TaxFormDetailModel(
    var id: Int,
    var title: String? = null,
    var imageUri: Uri? = null,
    var imageName: String? = null,
    var amount: String? = null,
    var isOtherDocument: Boolean = false
)
