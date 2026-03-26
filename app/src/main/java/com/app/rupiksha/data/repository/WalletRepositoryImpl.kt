package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.WalletRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : WalletRepository {

    override suspend fun fetchUser(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getFetchUser(headers, requestBody).execute() }
    }

    override suspend fun doWalletTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getdowalletTransaction(headers, requestBody).execute() }
    }

    override suspend fun getWalletBalance(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getWalletBalance(headers).execute() }
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