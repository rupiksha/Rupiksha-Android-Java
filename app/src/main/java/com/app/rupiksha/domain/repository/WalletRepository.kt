package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface WalletRepository {
    suspend fun fetchUser(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun doWalletTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getWalletBalance(headers: Map<String, String>): Resource<BaseResponse>
}