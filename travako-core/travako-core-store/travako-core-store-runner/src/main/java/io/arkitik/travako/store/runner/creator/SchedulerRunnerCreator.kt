package io.arkitik.travako.store.runner.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerCreator : StoreIdentityCreator<String, SchedulerRunnerDomain> {
    fun String.runnerKey(): SchedulerRunnerCreator
    fun String.runnerHost(): SchedulerRunnerCreator
    fun InstanceState.instanceState(): SchedulerRunnerCreator
    fun ServerDomain.server(): SchedulerRunnerCreator
}
