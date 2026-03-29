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
        return safeApiCall { api.getBankList().execute() }
    }

    override suspend fun getDeviceList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getDeiceList().execute() }
    }

    override suspend fun balanceEnquiry(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsBalanceEnquries(headers, requestBody).execute() }
    }

    override suspend fun cashWithdrawal(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsCashWithdrawl(headers, requestBody).execute() }
    }

    override suspend fun miniStatement(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.aepsministatement(headers, requestBody).execute() }
    }

    override suspend fun aadharPay(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        // Casting issue previously, let's fix it by using PartMap if needed or Body
        // ApiInterface shows: Call<BaseResponse> aepsaadharPay(@HeaderMap Map<String, String> headers, @PartMap() Map<String, RequestBody> partMa);
        // It's defined as @Multipart @POST("aeps/aadhar-pay")
        return Resource.Error("Multipart mapping needed for Aadhar Pay")
    }

    override suspend fun getStateList(): Resource<BaseResponse> {
        return safeApiCall { api.getStateList().execute() }
    }

    override suspend fun submitAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse> {
        // In ApiInterface: Call<BaseResponse> getDokyc(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedshopImage);
        return safeApiCall { api.getDokyc(headers, HashMap(map), shopImage).execute() }
    }

    override suspend fun updateAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse> {
        // In ApiInterface: Call<BaseResponse> getUpdateAepsKyc(@HeaderMap Map<String, String> headers, @PartMap() HashMap<String, RequestBody> map, @Part MultipartBody.Part uploadedshopImage);
        return safeApiCall { api.getUpdateAepsKyc(headers, HashMap(map), shopImage).execute() }
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
