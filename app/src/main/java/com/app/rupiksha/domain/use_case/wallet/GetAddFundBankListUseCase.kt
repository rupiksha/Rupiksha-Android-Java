package com.app.rupiksha.domain.use_case.wallet

import com.app.rupiksha.domain.repository.WalletRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetAddFundBankListUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getAddFundBankList(headers)
    }
}
