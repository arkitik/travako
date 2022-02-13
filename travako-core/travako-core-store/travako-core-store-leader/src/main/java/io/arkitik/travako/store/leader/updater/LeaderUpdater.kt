package io.arkitik.travako.store.leader.updater

import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:40 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderUpdater : StoreIdentityUpdater<String, LeaderDomain> {
    fun SchedulerRunnerDomain.assignToRunner(): LeaderUpdater
    fun LocalDateTime.lastModifiedDate(): LeaderUpdater
}
