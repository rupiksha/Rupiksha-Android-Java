package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.UtiRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class UtiRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : UtiRepository {

    override suspend fun getUtiStatus(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getUtiStatus(headers) }
    }

    override suspend fun psaRegistration(
        headers: Map<String, String>,
        map: Map<String, RequestBody>
    ): Resource<BaseResponse> {
        return safeApiCall { api.getPSARegistration(headers, HashMap(map)) }
    }

    override suspend fun buyCoupon(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.doBuyCoupon(headers, requestBody) }
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
