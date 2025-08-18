package io.arkitik.travako.adapter.exposed.runner.updater

import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunner
import io.arkitik.travako.store.runner.updater.SchedulerRunnerUpdater
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:22 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerUpdaterImpl(
    private val entity: TravakoSchedulerRunner,
) : SchedulerRunnerUpdater {
    override fun InstanceState.instanceState(): SchedulerRunnerUpdater {
        entity.instanceState = this
        return this@SchedulerRunnerUpdaterImpl
    }

    override fun LocalDateTime.lastHeartbeatTime(): SchedulerRunnerUpdater {
        entity.lastHeartbeatTime = this
        return this@SchedulerRunnerUpdaterImpl
    }

    override fun clearHeartbeat(): SchedulerRunnerUpdater {
        entity.lastHeartbeatTime = null
        return this@SchedulerRunnerUpdaterImpl
    }

    override fun update() = entity
}
