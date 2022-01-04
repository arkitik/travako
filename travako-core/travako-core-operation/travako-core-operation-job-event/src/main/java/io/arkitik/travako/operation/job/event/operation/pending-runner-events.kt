package io.arkitik.travako.operation.job.event.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.job.event.dto.EventDataDto
import io.arkitik.travako.sdk.job.event.dto.EventsDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyDto
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:33 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class PendingEventsForRunnerOperationProvider(
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val jobEventStoreQuery: JobEventStoreQuery,
    private val runnerJobEventStateStoreQuery: RunnerJobEventStateStoreQuery,
) {
    val pendingEventsForRunner = operationBuilder<JobEventRunnerKeyDto, EventsDto> {
        mainOperation {
            val schedulerRunnerDomain = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                RunnerDomainDto(
                    serverKey = serverKey,
                    runnerKey = runnerKey
                ))
            val jobEvents = jobEventStoreQuery.findAllPendingEventsForServer(schedulerRunnerDomain.server)
            val events = jobEvents
                .filter { event ->
                    !runnerJobEventStateStoreQuery.existsByRunnerAndEvent(schedulerRunnerDomain, event)
                }.map { event ->
                    EventDataDto(
                        eventUuid = event.uuid,
                        jobKey = event.jobInstance.jobKey,
                        eventType = event.eventType.name
                    )
                }
            EventsDto(events)
        }
    }
}
