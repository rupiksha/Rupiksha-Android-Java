package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface QuickTransferRepository {
    suspend fun getGlobalBankList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun fetchAccounts(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun accountVerify(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun initiateTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun doTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
}
