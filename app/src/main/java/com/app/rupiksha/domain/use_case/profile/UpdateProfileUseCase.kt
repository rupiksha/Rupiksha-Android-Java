package com.app.rupiksha.domain.use_case.profile

import com.app.rupiksha.domain.repository.ProfileRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        headers: Map<String, String>,
        partMap: Map<String, RequestBody>,
        profileImage: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return repository.updateProfile(headers, partMap, profileImage)
    }
}
