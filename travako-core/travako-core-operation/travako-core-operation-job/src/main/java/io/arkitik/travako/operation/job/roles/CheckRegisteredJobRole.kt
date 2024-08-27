package io.arkitik.travako.operation.job.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class CheckRegisteredJobRole(
    jobInstanceStoreQuery: JobInstanceStoreQuery,
    serverDomainSdk: ServerDomainSdk,
) : OperationRole<JobKeyDto, Unit> {
    private val jobRegisteredRole = JobRegisteredRole(jobInstanceStoreQuery, serverDomainSdk)
    override fun JobKeyDto.operateRole() {
        val jobRegistered = jobRegisteredRole.operateRole(this)
        if (jobRegistered) {
            throw JobErrors.JOB_ALREADY_REGISTERED.unprocessableEntity()
        }
    }
}
