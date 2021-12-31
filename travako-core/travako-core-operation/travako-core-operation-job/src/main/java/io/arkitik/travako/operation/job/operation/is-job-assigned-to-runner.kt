package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:37 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class IsJobAssignedToRunnerRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
) : OperationRole<JobServerRunnerKeyDto, Boolean> {
    override fun JobServerRunnerKeyDto.operateRole(): Boolean {
        return jobInstanceStoreQuery.existsByServerKeyAndAssignedToRunnerKeyAndJobKey(
            serverKey = serverKey,
            runnerKey = runnerKey,
            jobKey = jobKey
        )
    }

}
