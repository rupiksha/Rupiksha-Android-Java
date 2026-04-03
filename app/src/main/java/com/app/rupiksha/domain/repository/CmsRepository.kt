package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface CmsRepository {
    suspend fun getCmsService(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
