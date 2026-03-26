package com.app.rupiksha.domain.use_case.recharge

import com.app.rupiksha.domain.repository.RechargeRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class FetchOperatorUseCase @Inject constructor(
    private val repository: RechargeRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.fetchOperator(headers, requestBody)
    }
}