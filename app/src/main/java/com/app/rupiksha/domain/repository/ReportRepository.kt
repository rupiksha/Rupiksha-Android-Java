package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface ReportRepository {
    suspend fun getAllReportTypes(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun getReport(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getCommissionPlan(headers: Map<String, String>): Resource<BaseResponse>
}
