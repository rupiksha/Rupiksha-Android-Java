package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PayoutRepository {
    suspend fun getPayoutBankList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun addPayoutAccount(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?
    ): Resource<BaseResponse>
    suspend fun deletePayoutAccount(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun initiatePayoutTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun doPayoutTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
