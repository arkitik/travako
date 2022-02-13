package io.arkitik.travako.adapter.leader.updater

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
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
        leader.runner = this as TravakoSchedulerRunner
        return this@LeaderUpdaterImpl
    }

    override fun LocalDateTime.lastModifiedDate(): LeaderUpdater {
        leader.lastModifiedDate = this
        return this@LeaderUpdaterImpl
    }

    override fun update() = leader
}
