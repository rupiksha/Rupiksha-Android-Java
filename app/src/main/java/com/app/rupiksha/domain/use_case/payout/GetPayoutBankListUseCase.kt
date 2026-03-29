package com.app.rupiksha.domain.use_case.payout

import com.app.rupiksha.domain.repository.PayoutRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetPayoutBankListUseCase @Inject constructor(
    private val repository: PayoutRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getPayoutBankList(headers)
    }
}
