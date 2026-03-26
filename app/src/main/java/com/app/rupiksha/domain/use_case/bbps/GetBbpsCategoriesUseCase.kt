package com.app.rupiksha.domain.use_case.bbps

import com.app.rupiksha.domain.repository.BbpsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetBbpsCategoriesUseCase @Inject constructor(
    private val repository: BbpsRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getBbpsCategories(headers)
    }
}
