package io.arkitik.travako.store.job.event.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface RunnerJobEventStateStoreQuery : StoreQuery<String, RunnerJobEventStateDomain> {
    fun existsByRunnerAndEvent(
        schedulerRunner: SchedulerRunnerDomain,
        jobEvent: JobEventDomain,
    ): Boolean

    fun countByEvent(
        jobEvent: JobEventDomain,
    ): Long
}
