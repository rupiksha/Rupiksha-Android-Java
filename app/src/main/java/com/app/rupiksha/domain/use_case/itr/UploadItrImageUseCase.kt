package com.app.rupiksha.domain.use_case.itr

import com.app.rupiksha.domain.repository.ItrRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadItrImageUseCase @Inject constructor(
    private val repository: ItrRepository
) {
    suspend operator fun invoke(
        partMap: Map<String, RequestBody>,
        headers: Map<String, String>,
        file: MultipartBody.Part?
    ): Resource<BaseResponse> {
        return repository.uploadImage(partMap, headers, file)
    }
}
