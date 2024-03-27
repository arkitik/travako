package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
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
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val assignedRunnerJobs = operationBuilder<JobRunnerKeyDto, AssignJobsToRunnerDto> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
                .runOperation(
                    RunnerDomainDto(
                        server = server,
                        runnerKey = runnerKey,
                        runnerHost = runnerHost
                    )
                )
            val jobKeys = jobInstanceStoreQuery.findAllByServerAndRunner(
                server = server,
                runner = schedulerRunner,
            ).map {
                it.jobKey
            }
            AssignJobsToRunnerDto(
                serverKey = serverKey,
                runnerKey = runnerKey,
                runnerHost = runnerHost,
                jobKeys = jobKeys,
            )
        }
    }
}
