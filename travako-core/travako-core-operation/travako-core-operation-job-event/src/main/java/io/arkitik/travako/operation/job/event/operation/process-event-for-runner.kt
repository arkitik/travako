package io.arkitik.travako.operation.job.event.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.operation.job.event.errors.JobEventErrors
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
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
    private val serverDomainSdk: ServerDomainSdk,
) {
    val markEventProcessedForRunner = operationBuilder<JobEventRunnerKeyAndUuidDto, Unit> {
        mainOperation {
            val jobEvent = jobEventStore.storeQuery.find(eventUuid)
                ?: throw JobEventErrors.JOB_EVENT_IS_NOT_REGISTERED.unprocessableEntity()
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))

            val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                ))

            with(runnerJobEventStateStore) {
                storeCreator(identityCreator()) {
                    jobEvent.jobEvent()
                    schedulerRunner.runner()
                    create()
                }.save()
            }
            val schedulerRunnerDomains = schedulerRunnerDomainSdk.fetchServerSchedulerRunners
                .runOperation(server)
            val processedCount = runnerJobEventStateStore.storeQuery.countByEvent(jobEvent)
            if (schedulerRunnerDomains.size.toLong() == processedCount) {
                with(jobEventStore) {
                    storeUpdater(jobEvent.identityUpdater()) {
                        processed()
                        update()
                    }.save()
                }
            }
        }
    }
}
