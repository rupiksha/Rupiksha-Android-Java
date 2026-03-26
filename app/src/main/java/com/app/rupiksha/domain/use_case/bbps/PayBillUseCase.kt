package com.app.rupiksha.domain.use_case.bbps

import com.app.rupiksha.domain.repository.BbpsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class PayBillUseCase @Inject constructor(
    private val repository: BbpsRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.payBill(headers, requestBody)
    }
}
