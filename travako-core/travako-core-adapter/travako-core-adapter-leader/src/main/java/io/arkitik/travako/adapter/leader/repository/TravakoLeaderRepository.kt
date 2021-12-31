package io.arkitik.travako.adapter.leader.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.leader.TravakoLeader
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:52 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoLeaderRepository : RadixRepository<String, TravakoLeader> {
    fun findByServerServerKey(serverKey: String): TravakoLeader?
    fun existsByServerServerKey(serverKey: String): Boolean

    fun existsByServerServerKeyAndRunnerRunnerKeyAndLastModifiedDateBefore(
        serverKey: String,
        runnerKey: String,
        lastModifiedDate: LocalDateTime,
    ): Boolean
}
