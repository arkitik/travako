package io.arkitik.travako.store.runner.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:11 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerUpdater : StoreIdentityUpdater<String, SchedulerRunnerDomain> {
    fun InstanceState.instanceState(): SchedulerRunnerUpdater
    fun LocalDateTime.lastHeartbeatTime(): SchedulerRunnerUpdater
    fun clearHeartbeat(): SchedulerRunnerUpdater
}
