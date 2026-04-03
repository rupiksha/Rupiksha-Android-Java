package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.WalletRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : WalletRepository {

    override suspend fun fetchUser(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.getFetchUser(headers, requestBody) }
    }

    override suspend fun doWalletTransaction(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.getdowalletTransaction(headers, requestBody) }
    }

    override suspend fun getWalletBalance(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getWalletBalance(headers) }
    }

    override suspend fun getAddFundBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getfundbanklist(headers) }
    }

    override suspend fun addMoney(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        proofImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return safeApiCall { api.getAddMoney(HashMap(headers), HashMap(map), proofImage!!) }
    }

    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
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
