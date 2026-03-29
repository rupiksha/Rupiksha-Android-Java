package com.app.rupiksha.domain.use_case.aeps

import com.app.rupiksha.domain.repository.AepsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetStateListUseCase @Inject constructor(
    private val repository: AepsRepository
) {
    suspend operator fun invoke(): Resource<BaseResponse> {
        return repository.getStateList()
    }
}
