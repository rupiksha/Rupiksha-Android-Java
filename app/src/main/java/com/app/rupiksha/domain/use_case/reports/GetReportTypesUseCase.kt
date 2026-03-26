package com.app.rupiksha.domain.use_case.reports

import com.app.rupiksha.domain.repository.ReportRepository
import com.app.rupiksha.domain.util.Resource
import com.app.rupiksha.models.BaseResponse
import javax.inject.Inject

class GetReportTypesUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(headers: Map<String, String>): Resource<BaseResponse> {
        return repository.getAllReportTypes(headers)
    }
}