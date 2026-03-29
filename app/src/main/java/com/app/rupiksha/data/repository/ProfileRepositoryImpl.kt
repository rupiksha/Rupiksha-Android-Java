package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.ProfileRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : ProfileRepository {

    override suspend fun getUserInfo(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getUserInfo(headers).execute() }
    }

    override suspend fun updateProfile(
        headers: Map<String, String>,
        partMap: Map<String, RequestBody>,
        profileImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return safeApiCall { api.editProfile(headers, HashMap(partMap), profileImage).execute() }
    }

    override suspend fun getAddMoneyBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getfundbanklist(headers).execute() }
    }

    override suspend fun addMoney(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        proofImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return safeApiCall { api.getAddMoney(HashMap(headers), HashMap(map), proofImage).execute() }
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
