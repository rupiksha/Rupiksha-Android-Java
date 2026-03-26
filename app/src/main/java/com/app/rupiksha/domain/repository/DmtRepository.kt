package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody

interface DmtRepository {
    suspend fun remitterLogin(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun remitterRegister(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun getDmtBankList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun addDmtAccount(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun deleteDmtAccount(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun initiateDMTTransaction(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun doDMTTransaction(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun logoutDmt(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun validateAadhar(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun validateOtp(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>

    suspend fun biometricVerify(
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Resource<BaseResponse>
}
