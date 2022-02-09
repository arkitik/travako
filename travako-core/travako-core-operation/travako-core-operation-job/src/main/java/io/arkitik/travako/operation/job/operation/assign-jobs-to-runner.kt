package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.operation.job.roles.CheckJobsRegisteredRole
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.sdk.job.dto.AssignJobsToRunnerDto
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:51 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class AssignJobsToRunnerOperationProvider(
    private val jobInstanceStore: JobInstanceStore,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val serverDomainSdk: ServerDomainSdk,
) {
    val assignJobsToRunner = operationBuilder<AssignJobsToRunnerDto, Unit> {
        install(CheckJobsRegisteredRole(jobInstanceStore.storeQuery, serverDomainSdk))
        mainOperation {
            with(jobInstanceStore) {
                val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
                val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner
                    .runOperation(
                        RunnerDomainDto(
                            server = server,
                            runnerKey = runnerKey,
                            runnerHost = runnerHost
                        ))
                storeQuery.findAllByServerAndJobKeys(
                    server = server,
                    jobKeys = jobKeys
                ).map {
                    storeUpdater(it.identityUpdater()) {
                        schedulerRunner.assignToRunner()
                        update()
                    }
                }.save()
            }
        }
    }
}
