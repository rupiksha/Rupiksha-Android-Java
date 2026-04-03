package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.PayoutRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PayoutRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : PayoutRepository {

    override suspend fun getPayoutBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getPayoutBank(headers) }
    }

    override suspend fun addPayoutAccount(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return if (panImage != null) {
            safeApiCall { api.addPayoutAccount(headers, HashMap(map), panImage) }
        } else {
            Resource.Error("PAN image is required")
        }
    }

    override suspend fun deletePayoutAccount(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.deletePayoutAccount(headers, requestBody) }
    }

    override suspend fun initiatePayoutTransaction(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.initiatePayoutTransaction(headers, requestBody) }
    }

    override suspend fun doPayoutTransaction(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.dopayouttransaction(headers, requestBody) }
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
