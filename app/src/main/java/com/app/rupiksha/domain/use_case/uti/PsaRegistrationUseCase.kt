package com.app.rupiksha.domain.use_case.uti

import com.app.rupiksha.domain.repository.UtiRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class PsaRegistrationUseCase @Inject constructor(
    private val repository: UtiRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, map: Map<String, RequestBody>): Resource<BaseResponse> {
        return repository.psaRegistration(headers, map)
    }
}
