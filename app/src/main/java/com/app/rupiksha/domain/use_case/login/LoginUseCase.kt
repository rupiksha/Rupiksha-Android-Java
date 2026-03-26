package com.app.rupiksha.domain.use_case.login

import com.app.rupiksha.domain.repository.UserRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(requestBody: RequestBody): Resource<BaseResponse> {
        return repository.login(requestBody)
    }
}