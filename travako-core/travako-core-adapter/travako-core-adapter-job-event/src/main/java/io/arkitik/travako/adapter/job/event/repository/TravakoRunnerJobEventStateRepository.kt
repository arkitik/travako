package io.arkitik.travako.adapter.job.event.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.entity.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:06 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoRunnerJobEventStateRepository : RadixRepository<String, TravakoRunnerJobEventState> {
    fun existsByJobEventAndRunner(
        jobEvent: TravakoJobEvent,
        runner: TravakoSchedulerRunner,
    ): Boolean

    fun countAllByJobEvent(jobEvent: TravakoJobEvent): Long

    fun deleteAllByRunnerServerAndJobEventProcessedFlag(
        server: ServerDomain,
        processedFlag: Boolean,
    )

    fun deleteAllByRunnerServerAndRunnerInstanceState(
        server: ServerDomain,
        instanceState: InstanceState,
    )
}
