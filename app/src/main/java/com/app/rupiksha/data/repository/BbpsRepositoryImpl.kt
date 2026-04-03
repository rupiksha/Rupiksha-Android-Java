package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.BbpsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class BbpsRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : BbpsRepository {

    override suspend fun getBbpsCategories(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getbbpsService(headers) }
    }

    override suspend fun getBillerList(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getBillList(headers, requestBody) }
    }

    override suspend fun fetchBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getFetchBill(headers, requestBody) }
    }

    override suspend fun payBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getPayBill(headers, requestBody) }
    }

    override suspend fun getBbpsReports(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getReport(headers, requestBody) }
    }

    private suspend fun <T> safeApiCall(call: suspend () -> retrofit2.Response<T>): Resource<T> {
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
