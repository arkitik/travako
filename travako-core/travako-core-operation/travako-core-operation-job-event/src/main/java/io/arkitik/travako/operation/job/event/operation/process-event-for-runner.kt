package io.arkitik.travako.operation.job.event.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.operation.job.event.errors.JobEventErrors
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.runner.dto.RunnerServerDomainDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyAndUuidDto
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:26 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class OperationProviderMarkEventProcessedForRunner(
    private val jobEventStore: JobEventStore,
    private val runnerJobEventStateStore: RunnerJobEventStateStore,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
) {
    val markEventProcessedForRunner = operationBuilder<JobEventRunnerKeyAndUuidDto, Unit> {
        mainOperation {
            val jobEventDomain = jobEventStore.storeQuery.find(eventUuid)
                ?: throw JobEventErrors.JOB_EVENT_IS_NOT_REGISTERED.unprocessableEntity()

            val schedulerRunnerDomain = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                RunnerDomainDto(
                    serverKey = serverKey,
                    runnerKey = runnerKey
                ))

            with(runnerJobEventStateStore) {
                storeCreator(identityCreator()) {
                    jobEventDomain.jobEvent()
                    schedulerRunnerDomain.runner()
                    create()
                }.save()
            }
            val schedulerRunnerDomains = schedulerRunnerDomainSdk.fetchServerSchedulerRunners.runOperation(
                RunnerServerDomainDto(serverKey)
            )
            val processedCount = runnerJobEventStateStore.storeQuery.countByEvent(jobEventDomain)
            if (schedulerRunnerDomains.size.toLong() == processedCount) {
                with(jobEventStore) {
                    storeUpdater(jobEventDomain.identityUpdater()) {
                        processed()
                        update()
                    }.save()
                }
            }
        }
    }
}
