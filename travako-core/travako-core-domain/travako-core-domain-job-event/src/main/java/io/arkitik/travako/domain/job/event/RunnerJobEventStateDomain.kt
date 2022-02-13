package io.arkitik.travako.domain.job.event

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:29 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface RunnerJobEventStateDomain : Identity<String> {
    override val uuid: String
    val runner: SchedulerRunnerDomain
    val jobEvent: JobEventDomain
}
