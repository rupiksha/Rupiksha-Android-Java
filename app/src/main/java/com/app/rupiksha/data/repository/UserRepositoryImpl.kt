package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : UserRepository {

    override suspend fun login(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.login(requestBody) }
    }

    override suspend fun otpVerify(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.otpVerify(requestBody) }
    }

    override suspend fun getRole(): Resource<BaseResponse> {
        return safeApiCall { api.getrole() }
    }

    override suspend fun registration(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.registration(requestBody) }
    }

    override suspend fun getUserInfo(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getUserInfo(headers) }
    }

    override suspend fun forgetPassword(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.forgetpassword(requestBody) }
    }

    override suspend fun forgetPin(requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.forgetpin(requestBody) }
    }

    override suspend fun getSupport(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getSupport(headers) }
    }

    override suspend fun changePassword(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.changePassword(headers, requestBody) }
    }

    override suspend fun changePin(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse> {
        return safeApiCall { api.changePin(headers, requestBody) }
    }

    override suspend fun getDocumentList(): Resource<BaseResponse> {
        return safeApiCall { api.getDocumentList() }
    }

    override suspend fun submitUserKyc(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?,
        aadharFront: MultipartBody.Part?,
        aadharBack: MultipartBody.Part?,
        docImage: MultipartBody.Part?,
        selfie: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return safeApiCall {
            api.getuserKyc(
                HashMap(headers),
                HashMap(map),
                panImage!!,
                aadharFront!!,
                aadharBack!!,
                docImage!!,
                selfie!!
            )
        }
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
