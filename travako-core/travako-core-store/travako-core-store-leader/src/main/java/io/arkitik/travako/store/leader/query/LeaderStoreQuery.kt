package io.arkitik.travako.store.leader.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:39 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderStoreQuery : StoreQuery<String, LeaderDomain> {
    fun findByServer(
        server: ServerDomain,
    ): LeaderDomain?

    fun existsByServer(
        server: ServerDomain,
    ): Boolean

    fun existsByServerAndRunnerAndBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        beforeDate: LocalDateTime,
    ): Boolean
}
