package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface SupportRepository {
    suspend fun getSupportInfo(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun getSupportTypes(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun createTicket(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
