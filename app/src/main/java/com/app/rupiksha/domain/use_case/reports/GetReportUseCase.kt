package com.app.rupiksha.domain.use_case.reports

import com.app.rupiksha.domain.repository.ReportRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import okhttp3.RequestBody
import javax.inject.Inject

class GetReportUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(headers: Map<String, String>, requestBody: RequestBody): Resource<BaseResponse> {
        return repository.getReport(headers, requestBody)
    }
}