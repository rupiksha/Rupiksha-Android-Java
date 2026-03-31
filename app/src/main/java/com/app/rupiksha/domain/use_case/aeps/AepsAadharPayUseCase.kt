package com.app.rupiksha.domain.use_case.aeps

import com.app.rupiksha.domain.repository.AepsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class AepsAadharPayUseCase @Inject constructor(
    private val repository: AepsRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.aadharPay(headers, requestBody)
    }
}
