package com.app.rupiksha.domain.use_case.login

import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetDocumentListUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Resource<BaseResponse> {
        return repository.getDocumentList()
    }
}
