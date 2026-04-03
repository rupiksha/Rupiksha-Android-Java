package com.app.rupiksha.domain.use_case.recharge

import com.app.rupiksha.domain.repository.RechargeRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetOperatorsUseCase @Inject constructor(
    private val repository: RechargeRepository
) {
    suspend operator fun invoke(type: String, headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getOperators(type, headers)
    }
}