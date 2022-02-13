package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.JobRunnerKeyDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:51 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RemoveRunnerJobsAssigneeOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val removeRunnerJobsAssignee = operationBuilder<JobRunnerKeyDto, Unit> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
                .runOperation(RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                ))
            with(jobInstanceStore) {
                storeQuery.findAllByServerAndRunner(
                    server = server,
                    runner = schedulerRunner,
                ).map {
                    storeUpdater(it.identityUpdater()) {
                        removeRunnerAssignee()
                        update()
                    }
                }.save()
            }
        }
    }
}
