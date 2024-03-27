package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:37 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class IsJobAssignedToRunnerRole(
    private val jobInstanceStoreQuery: JobInstanceStoreQuery,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
) : OperationRole<JobServerRunnerKeyDto, Boolean> {
    override fun JobServerRunnerKeyDto.operateRole(): Boolean {
        val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
        val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
            .runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                )
            )
        return jobInstanceStoreQuery.existsByServerAndAssignedToRunnerAndJobKey(
            server = server,
            runner = schedulerRunner,
            jobKey = jobKey
        )
    }

}
