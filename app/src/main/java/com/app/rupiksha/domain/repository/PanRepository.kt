package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface PanRepository {
    suspend fun verifyPan(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
