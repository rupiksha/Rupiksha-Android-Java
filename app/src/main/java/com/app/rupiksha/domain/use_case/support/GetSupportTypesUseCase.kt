package com.app.rupiksha.domain.use_case.support

import com.app.rupiksha.domain.repository.SupportRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetSupportTypesUseCase @Inject constructor(
    private val repository: SupportRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getSupportTypes(headers)
    }
}
