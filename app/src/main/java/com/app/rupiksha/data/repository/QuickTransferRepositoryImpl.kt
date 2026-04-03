package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.QuickTransferRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class QuickTransferRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : QuickTransferRepository {

    override suspend fun getGlobalBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getbankifsc(headers) }
    }

    override suspend fun fetchAccounts(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.fetchaccount(headers, requestBody) }
    }

    override suspend fun accountVerify(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.qtaccountverify(headers, requestBody) }
    }

    override suspend fun initiateTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.initiateqttransaction(headers, requestBody) }
    }

    override suspend fun doTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.doqttransaction(headers, requestBody) }
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
