package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.AepsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AepsRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : AepsRepository {

    override suspend fun getBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getBankList() }
    }

    override suspend fun getDeviceList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getDeiceList() }
    }

    override suspend fun balanceEnquiry(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsBalanceEnquries(headers, requestBody) }
    }

    override suspend fun cashWithdrawal(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsCashWithdrawl(headers, requestBody) }
    }

    override suspend fun miniStatement(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsministatement(headers, requestBody) }
    }

    override suspend fun aadharPay(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return if (requestBody is MultipartBody) {
            val partMap = mutableMapOf<String, RequestBody>()
            requestBody.parts.forEach { part ->
                val name = part.headers?.get("Content-Disposition")?.let { header ->
                    val match = Regex("name=\"(.*?)\"").find(header)
                    match?.groupValues?.get(1)
                }
                if (name != null) {
                    partMap[name] = part.body
                }
            }
            safeApiCall { api.aepsaadharPay(headers, partMap) }
        } else {
            Resource.Error("Expected MultipartBody for Aadhar Pay")
        }
    }

    override suspend fun getStateList(): Resource<BaseResponse> {
        return safeApiCall { api.getStateList() }
    }

    override suspend fun submitAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse> {
        return if (shopImage != null) {
            safeApiCall { api.getDokyc(headers, HashMap(map), shopImage) }
        } else {
            Resource.Error("Shop image is required")
        }
    }

    override suspend fun updateAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse> {
        return if (shopImage != null) {
            safeApiCall { api.getUpdateAepsKyc(headers, HashMap(map), shopImage) }
        } else {
            Resource.Error("Shop image is required")
        }
    }

    override suspend fun verifyTwoFactor(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.verifyTwoFAFingurePrint(headers, requestBody) }
    }

    override suspend fun verifyApTwoFactor(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.verifyAPTwoFAFingurePrint(headers, requestBody) }
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
