package com.app.rupiksha.domain.use_case.payout

import com.app.rupiksha.domain.repository.PayoutRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class InitiatePayoutTransactionUseCase @Inject constructor(
    private val repository: PayoutRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.initiatePayoutTransaction(headers, requestBody)
    }
}
