package com.app.rupiksha.domain.use_case.aeps

import com.app.rupiksha.domain.repository.AepsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetBankListUseCase @Inject constructor(
    private val repository: AepsRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getBankList(headers)
    }
}
