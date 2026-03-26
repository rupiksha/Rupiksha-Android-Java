package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.ReportRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : ReportRepository {

    override suspend fun getAllReportTypes(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getAllReportType(headers).execute() }
    }

    override suspend fun getReport(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getReport(headers, requestBody).execute() }
    }

    private fun <T> safeApiCall(call: () -> retrofit2.Response<T>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}