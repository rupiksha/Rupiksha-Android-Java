package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.ItrRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ItrRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : ItrRepository {

    override suspend fun uploadImage(
        partMap: Map<String, RequestBody>,
        headers: Map<String, String>,
        file: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return if (file != null) {
            safeApiCall { api.uploadImage(partMap, HashMap(headers), file) }
        } else {
            Resource.Error("File is required")
        }
    }

    override suspend fun submitItrForm(
        partMap: Map<String, RequestBody>,
        headers: Map<String, String>
    ): Resource<BaseResponse> {
        return safeApiCall { api.submitItrForm(partMap, HashMap(headers)) }
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
