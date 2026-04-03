package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ItrRepository {
    suspend fun uploadImage(
        partMap: Map<String, RequestBody>,
        headers: Map<String, String>,
        file: MultipartBody.Part?
    ): Resource<BaseResponse>

    suspend fun submitItrForm(
        partMap: Map<String, RequestBody>,
        headers: Map<String, String>
    ): Resource<BaseResponse>
}
