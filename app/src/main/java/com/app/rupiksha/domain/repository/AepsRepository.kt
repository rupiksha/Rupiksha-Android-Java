package com.app.rupiksha.domain.repository

import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AepsRepository {
    suspend fun getBankList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun getDeviceList(headers: Map<String, String>): Resource<BaseResponse>
    suspend fun balanceEnquiry(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun cashWithdrawal(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun miniStatement(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun aadharPay(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse>
    suspend fun getStateList(): Resource<BaseResponse>
    suspend fun submitAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse>
    suspend fun updateAepsKyc(headers: Map<String, String>, map: Map<String, RequestBody>, shopImage: MultipartBody.Part?): Resource<BaseResponse>
}
