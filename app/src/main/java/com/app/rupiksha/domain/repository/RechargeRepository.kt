package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface RechargeRepository {
    suspend fun getOperators(type: String, headers: Map<String, String>): Resource<BaseResponse>
    suspend fun fetchOperator(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun doRecharge(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}