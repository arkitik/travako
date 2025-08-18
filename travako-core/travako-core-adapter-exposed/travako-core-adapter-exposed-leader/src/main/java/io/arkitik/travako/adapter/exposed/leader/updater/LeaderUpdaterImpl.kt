package io.arkitik.travako.adapter.exposed.leader.updater

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.exposed.leader.TravakoLeader
import io.arkitik.travako.store.leader.updater.LeaderUpdater
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:59 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderUpdaterImpl(
    private val leader: TravakoLeader,
) : LeaderUpdater {
    override fun SchedulerRunnerDomain.assignToRunner(): LeaderUpdater {
        leader.runnerUuid = this.uuid
        return this@LeaderUpdaterImpl
    }

    override fun LocalDateTime.lastModifiedDate(): LeaderUpdater {
        leader.lastModifiedDate = this
        return this@LeaderUpdaterImpl
    }

    override fun update() = leader
}
