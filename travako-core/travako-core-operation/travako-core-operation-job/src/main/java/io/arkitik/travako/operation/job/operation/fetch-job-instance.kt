package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.domain.job.dto.JobDomainDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:16 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchJobInstanceOperationProvider(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) {
    val fetchJobInstance = operationBuilder<JobDomainDto, JobInstanceDomain> {
        mainOperation {
            with(jobInstanceStoreQuery) {
                findByServerAndJobKey(
                    server = server, jobKey = jobKey
                ) ?: throw JobErrors.JOB_IS_NOT_REGISTERED.unprocessableEntity()
            }
        }
    }
}
