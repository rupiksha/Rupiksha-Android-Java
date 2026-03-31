package com.app.rupiksha.domain.use_case.login

import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SubmitUserKycUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        panImage: MultipartBody.Part?,
        aadharFront: MultipartBody.Part?,
        aadharBack: MultipartBody.Part?,
        docImage: MultipartBody.Part?,
        selfie: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return repository.submitUserKyc(
            headers,
            map,
            panImage,
            aadharFront,
            aadharBack,
            docImage,
            selfie
        )
    }
}
