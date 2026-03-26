package com.app.rupiksha.domain.use_case.dmt

import com.app.rupiksha.domain.repository.DmtRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class BiometricVerifyUseCase @Inject constructor(
    private val repository: DmtRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        // Assuming we add biometricVerify to the repository as well
        return repository.biometricVerify(headers, requestBody)
    }
}
