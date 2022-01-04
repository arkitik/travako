package io.arkitik.travako.store.job.event.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface RunnerJobEventStateCreator : StoreIdentityCreator<String, RunnerJobEventStateDomain> {
    fun SchedulerRunnerDomain.runner(): RunnerJobEventStateCreator
    fun JobEventDomain.jobEvent(): RunnerJobEventStateCreator
}
