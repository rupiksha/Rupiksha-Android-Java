package com.app.rupiksha.domain.use_case.wallet

import com.app.rupiksha.domain.repository.WalletRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class DoWalletTransactionUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.doWalletTransaction(headers, requestBody)
    }
}