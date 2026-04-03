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
        return safeApiCall { api.getUserInfo(headers) }
    }

    override suspend fun updateProfile(
        headers: Map<String, String>,
        partMap: Map<String, RequestBody>,
        profileImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return if (profileImage != null) {
            safeApiCall { api.editProfile(headers, HashMap(partMap), profileImage) }
        } else {
            Resource.Error("Profile image is required")
        }
    }

    override suspend fun getAddMoneyBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getfundbanklist(headers) }
    }

    override suspend fun addMoney(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        proofImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return if (proofImage != null) {
            safeApiCall { api.getAddMoney(HashMap(headers), HashMap(map), proofImage) }
        } else {
            Resource.Error("Proof image is required")
        }
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
