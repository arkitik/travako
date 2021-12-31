package io.arkitik.travako.operation.job.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CheckRegisteredJobRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) : OperationRole<JobKeyDto, Unit> {
    override fun JobKeyDto.operateRole() {
        jobInstanceStoreQuery.existsByServerKeyAndJobKey(serverKey, jobKey)
            .takeIf { it }?.also {
                throw JobErrors.JOB_ALREADY_REGISTERED.unprocessableEntity()
            }
    }
}
