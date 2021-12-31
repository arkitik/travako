package io.arkitik.travako.operation.job.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.job.errors.JobErrors
import io.arkitik.travako.sdk.job.dto.AssignJobsToRunnerDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class CheckJobsRegisteredRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) : OperationRole<AssignJobsToRunnerDto, Unit> {
    override fun AssignJobsToRunnerDto.operateRole() {
        jobInstanceStoreQuery.existsAllByServerKeyAndJobKeys(
            serverKey,
            jobKeys
        ).takeIf { !it }?.also {
            throw JobErrors.ONE_OR_JOB_ARE_NOT_REGISTERED.unprocessableEntity()
        }
    }
}
