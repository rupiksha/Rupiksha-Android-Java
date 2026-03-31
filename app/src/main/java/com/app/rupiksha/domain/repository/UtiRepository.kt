package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface UtiRepository {
    suspend fun getUtiStatus(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun psaRegistration(headers: Map<String, String>, map: Map<String, RequestBody>): Resource<BaseResponse>
    suspend fun buyCoupon(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
