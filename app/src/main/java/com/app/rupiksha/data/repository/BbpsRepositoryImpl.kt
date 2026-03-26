package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.BbpsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class BbpsRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : BbpsRepository {

    override suspend fun getBbpsCategories(headers: Map<String, String>): Resource<BaseResponse> {
        return try {
            val response = api.getbbpsService(headers).execute()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getBillerList(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return try {
            val response = api.getBillList(headers, requestBody).execute()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun fetchBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return try {
            val response = api.getFetchBill(headers, requestBody).execute()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(response.message() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun payBill(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return try {
            val response = api.getPayBill(headers, requestBody).execute()
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
