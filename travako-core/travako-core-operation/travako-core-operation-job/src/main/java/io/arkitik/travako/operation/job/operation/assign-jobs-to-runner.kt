package io.arkitik.travako.operation.job.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.operation.job.roles.CheckJobsRegisteredRole
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
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
) {
    val assignJobsToRunner = operationBuilder<AssignJobsToRunnerDto, Unit> {
        install(CheckJobsRegisteredRole(jobInstanceStore.storeQuery))
        mainOperation {
            with(jobInstanceStore) {
                val schedulerRunnerDomain = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                    RunnerDomainDto(
                        serverKey = serverKey,
                        runnerKey = runnerKey
                    ))
                storeQuery.findAllByServerKeyAndJobKeys(
                    serverKey,
                    jobKeys
                ).map {
                    storeUpdater(it.identityUpdater()) {
                        schedulerRunnerDomain.assignToRunner()
                        update()
                    }
                }.save()
            }
        }
    }
}
