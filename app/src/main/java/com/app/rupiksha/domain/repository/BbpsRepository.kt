package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface BbpsRepository {
    suspend fun getBbpsCategories(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun getBillerList(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun fetchBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun payBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getBbpsReports(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
