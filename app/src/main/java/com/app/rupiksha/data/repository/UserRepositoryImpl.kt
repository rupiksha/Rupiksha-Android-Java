package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : UserRepository {

    override suspend fun login(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.login(requestBody).execute() }
    }

    override suspend fun otpVerify(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.otpVerify(requestBody).execute() }
    }

    override suspend fun getRole(): Resource<BaseResponse> {
        return safeApiCall { api.getrole().execute() }
    }

    override suspend fun registration(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.registration(requestBody).execute() }
    }

    override suspend fun getUserInfo(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getUserInfo(headers).execute() }
    }

    override suspend fun forgetPassword(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.forgetpassword(requestBody).execute() }
    }

    override suspend fun forgetPin(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.forgetpin(requestBody).execute() }
    }

    override suspend fun getSupport(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getSupport(headers).execute() }
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