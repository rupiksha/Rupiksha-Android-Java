package com.app.rupiksha.domain.use_case.profile

import com.app.rupiksha.domain.repository.ProfileRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getUserInfo(headers)
    }
}
