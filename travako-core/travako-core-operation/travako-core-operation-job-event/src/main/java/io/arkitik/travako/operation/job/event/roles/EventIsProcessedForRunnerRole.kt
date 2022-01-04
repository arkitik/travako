package io.arkitik.travako.operation.job.event.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.job.event.errors.JobEventErrors
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyAndUuidDto
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:03 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class EventIsProcessedForRunnerRole(
    private val jobEventStoreQuery: JobEventStoreQuery,
    private val schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    private val runnerJobEventStateStoreQuery: RunnerJobEventStateStoreQuery,
) : OperationRole<JobEventRunnerKeyAndUuidDto, Boolean> {
    override fun JobEventRunnerKeyAndUuidDto.operateRole(): Boolean {
        val schedulerRunnerDomain = schedulerRunnerDomainSdk.fetchSchedulerRunner.runOperation(
            RunnerDomainDto(serverKey, runnerKey)
        )
        val jobEventDomain = jobEventStoreQuery.find(
            uuid = eventUuid
        ) ?: throw JobEventErrors.JOB_EVENT_IS_NOT_REGISTERED.unprocessableEntity()
        return runnerJobEventStateStoreQuery.existsByRunnerAndEvent(
            schedulerRunnerDomain,
            jobEventDomain
        )
    }

}
