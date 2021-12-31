package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.travako.sdk.job.dto.AssignJobsToRunnerDto
import io.arkitik.travako.sdk.job.dto.JobRunnerKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:25 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class AssignedRunnerJobsOperationProvider(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) {
    val assignedRunnerJobs = operationBuilder<JobRunnerKeyDto, AssignJobsToRunnerDto> {
        mainOperation {
            val jobKeys = jobInstanceStoreQuery.findAllByServerKeyAndRunnerKey(
                serverKey, runnerKey
            ).map {
                it.jobKey
            }
            AssignJobsToRunnerDto(
                serverKey, runnerKey, jobKeys
            )
        }
    }
}
