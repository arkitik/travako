package io.arkitik.travako.adapter.redis.job.event.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.redis.job.event.repository.TravakoRunnerJobEventStateRepository
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.redis.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:10 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobEventStateStoreQueryImpl(
    private val travakoRunnerJobEventStateRepository: TravakoRunnerJobEventStateRepository,
) : StoreQueryImpl<String, RunnerJobEventStateDomain, TravakoRunnerJobEventState>(
    travakoRunnerJobEventStateRepository
), RunnerJobEventStateStoreQuery {
    override fun existsByRunnerAndEvent(
        schedulerRunner: SchedulerRunnerDomain,
        jobEvent: JobEventDomain,
    ) = travakoRunnerJobEventStateRepository.existsByJobEventUuidAndRunnerUuid(
        jobEventUuid = jobEvent.uuid,
        runnerUuid = schedulerRunner.uuid,
    )

    override fun countByEvent(jobEvent: JobEventDomain) =
        travakoRunnerJobEventStateRepository.findAllByJobEventUuid(
            jobEvent.uuid
        ).size.toLong()
}
