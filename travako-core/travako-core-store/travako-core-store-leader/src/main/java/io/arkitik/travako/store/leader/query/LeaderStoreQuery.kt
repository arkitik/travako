package io.arkitik.travako.store.leader.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.leader.LeaderDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:39 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface LeaderStoreQuery : StoreQuery<String, LeaderDomain> {
    fun findByServerKey(
        serverKey: String,
    ): LeaderDomain?

    fun existsByServerKey(
        serverKey: String,
    ): Boolean

    fun existsByServerKeyAndRunnerKeyAndBefore(
        serverKey: String,
        runnerKey: String,
        beforeDate: LocalDateTime,
    ): Boolean
}
