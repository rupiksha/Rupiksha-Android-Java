package com.app.rupiksha.domain.use_case.dmt

import com.app.rupiksha.domain.repository.DmtRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetDmtBankListUseCase @Inject constructor(
    private val repository: DmtRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getDmtBankList(headers)
    }
}
