package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileRepository {
    suspend fun getUserInfo(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun updateProfile(headers: Map<String, String>, partMap: Map<String, RequestBody>, profileImage: MultipartBody.Part?): Resource<BaseResponse>
    suspend fun getAddMoneyBankList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun addMoney(headers: Map<String, String>, map: Map<String, RequestBody>, proofImage: MultipartBody.Part?): Resource<BaseResponse>
}
