package com.app.rupiksha.domain.use_case.uti

import com.app.rupiksha.domain.repository.UtiRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetUtiStatusUseCase @Inject constructor(
    private val repository: UtiRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getUtiStatus(headers)
    }
}
