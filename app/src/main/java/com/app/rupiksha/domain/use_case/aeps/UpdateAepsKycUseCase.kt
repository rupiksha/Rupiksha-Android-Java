package com.app.rupiksha.domain.use_case.aeps

import com.app.rupiksha.domain.repository.AepsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateAepsKycUseCase @Inject constructor(
    private val repository: AepsRepository
) {
    suspend operator fun invoke(
        headers: Map<String, String>,
        map: Map<String, RequestBody>,
        shopImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return repository.updateAepsKyc(headers, map, shopImage)
    }
}
