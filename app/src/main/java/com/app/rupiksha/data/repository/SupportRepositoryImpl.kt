package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.SupportRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class SupportRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : SupportRepository {

    override suspend fun getSupportInfo(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getSupport(headers).execute() }
    }

    override suspend fun getSupportTypes(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getSupportType(headers).execute() }
    }

    override suspend fun createTicket(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.doRaiseComplained(headers, requestBody).execute() }
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
