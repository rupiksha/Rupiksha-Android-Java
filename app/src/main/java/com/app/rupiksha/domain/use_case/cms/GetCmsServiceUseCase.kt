package com.app.rupiksha.domain.use_case.cms

import com.app.rupiksha.domain.repository.CmsRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class GetCmsServiceUseCase @Inject constructor(
    private val repository: CmsRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.getCmsService(headers, requestBody)
    }
}
