package com.app.rupiksha.domain.use_case.qtransfer

import com.app.rupiksha.domain.repository.QuickTransferRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class InitiateQtTransactionUseCase @Inject constructor(
    private val repository: QuickTransferRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.initiateTransaction(headers, requestBody)
    }
}
