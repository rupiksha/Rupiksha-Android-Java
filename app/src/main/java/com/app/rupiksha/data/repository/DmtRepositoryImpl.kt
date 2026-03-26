package com.app.rupiksha.data.repository

import com.app.rupiksha.apis.ApiInterface
import com.app.rupiksha.domain.repository.DmtRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class DmtRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : DmtRepository {

    override suspend fun remitterLogin(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getremitterLogin(headers, requestBody).execute() }
    }

    override suspend fun remitterRegister(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getremitterRegister(headers, requestBody).execute() }
    }

    override suspend fun getDmtBankList(headers: Map<String, String>): Resource<BaseResponse> {
        return safeApiCall { api.getDmtBank(headers).execute() }
    }

    override suspend fun addDmtAccount(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.addDmtAccount(headers, requestBody).execute() }
    }

    override suspend fun deleteDmtAccount(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.deleteDmtAccount(headers, requestBody).execute() }
    }

    override suspend fun initiateDMTTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.initiateDMTTransaction(headers, requestBody).execute() }
    }

    override suspend fun doDMTTransaction(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.doDMTTransaction(headers, requestBody).execute() }
    }

    override suspend fun logoutDmt(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.logoutDmt(headers, requestBody).execute() }
    }

    override suspend fun validateAadhar(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getVAadharApi(headers, requestBody).execute() }
    }

    override suspend fun validateOtp(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return safeApiCall { api.getValidateOtpApi(headers, requestBody).execute() }
    }

    override suspend fun biometricVerify(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        // Looking at ApiInterface, it seems verifyFingurePrint or similar might be used for DMT KYC
        // But the presenter calls getBiometricVerify which uses verifyAepsOtp in some places or custom ones.
        // Actually, let's look at ApiInterface again for DMT biometric verify.
        // There is no explicit "dmt biometric verify" in ApiInterface. 
        // DMTKycActivity uses presenter.getBiometricVerify.
        // Let's check DMTKycPresenter.
        return safeApiCall { api.verifyFingurePrint(headers, requestBody).execute() }
    }

    private fun <T> safeApiCall(call: () -> retrofit2.Response<T>): Resource<T> {
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
