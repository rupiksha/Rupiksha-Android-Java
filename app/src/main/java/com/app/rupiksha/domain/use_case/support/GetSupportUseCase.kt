package com.app.rupiksha.domain.use_case.support

import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetSupportUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getSupport(headers)
    }
}