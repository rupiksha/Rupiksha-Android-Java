package com.app.rupiksha.domain.repository

import com.app.rupiksha.models.BaseResponse
import com.app.rupiksha.domain.util.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun login(requestBody: RequestBody): Resource<BaseResponse>
    suspend fun otpVerify(requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getRole(): Resource<BaseResponse>
    suspend fun registration(requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getUserInfo(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun forgetPassword(requestBody: RequestBody): Resource<BaseResponse>
    suspend fun forgetPin(requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getSupport(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun changePassword(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun changePin(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getDocumentList(): Resource<BaseResponse>
    suspend fun submitUserKyc(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?,
        aadharFront: MultipartBody.Part?,
        aadharBack: MultipartBody.Part?,
        docImage: MultipartBody.Part?,
        selfie: MultipartBody.Part?
    ): Resource<BaseResponse>
}
