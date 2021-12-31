package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobServerDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:57 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerJobsOperationProvider(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) {
    val serverJobs = operationBuilder<JobServerDto, List<JobDetails>> {
        mainOperation {
            jobInstanceStoreQuery.findAllByServerKey(serverKey)
                .map { job ->
                    JobDetails(
                        job.jobKey,
                        JobStatus.RUNNING == job.jobStatus,
                        job.lastRunningTime
                    )
                }
        }
    }
}
