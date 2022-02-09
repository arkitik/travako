package io.arkitik.travako.sdk.domain.runner

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.sdk.domain.runner.dto.RunnerDomainDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:12 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerDomainSdk {
    val fetchSchedulerRunner: Operation<RunnerDomainDto, SchedulerRunnerDomain>
    val fetchServerSchedulerRunners: Operation<ServerDomain, List<SchedulerRunnerDomain>>
    val fetchOldestHeartbeatRunner: Operation<ServerDomain, SchedulerRunnerDomain>
}
