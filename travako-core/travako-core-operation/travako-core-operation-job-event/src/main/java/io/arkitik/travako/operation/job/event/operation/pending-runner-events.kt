package io.arkitik.travako.operation.job.event.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
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
    private val serverDomainSdk: ServerDomainSdk,
) {
    val pendingEventsForRunner = operationBuilder<JobEventRunnerKeyDto, EventsDto> {
        mainOperation {
            val server = serverDomainSdk.fetchServer.runOperation(ServerDomainDto(serverKey))
            val schedulerRunner = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
                RunnerDomainDto(
                    server = server,
                    runnerKey = runnerKey,
                    runnerHost = runnerHost
                )
            )
            val jobEvents = jobEventStoreQuery.findAllPendingEventsForServer(server)
            val events = jobEvents
                .filter { event ->
                    !runnerJobEventStateStoreQuery.existsByRunnerAndEvent(schedulerRunner, event)
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
