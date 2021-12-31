package io.arkitik.travako.adapter.leader.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.store.leader.query.LeaderStoreQuery
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:54 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderStoreQueryImpl(
    private val travakoLeaderRepository: TravakoLeaderRepository,
) : StoreQueryImpl<String, LeaderDomain, TravakoLeader>(travakoLeaderRepository), LeaderStoreQuery {
    override fun findByServerKey(serverKey: String) =
        travakoLeaderRepository.findByServerServerKey(serverKey)

    override fun existsByServerKey(serverKey: String) =
        travakoLeaderRepository.existsByServerServerKey(serverKey)

    override fun existsByServerKeyAndRunnerKeyAndBefore(
        serverKey: String,
        runnerKey: String,
        beforeDate: LocalDateTime,
    ) = travakoLeaderRepository.existsByServerServerKeyAndRunnerRunnerKeyAndLastModifiedDateBefore(
        serverKey = serverKey, runnerKey = runnerKey, lastModifiedDate = beforeDate
    )
}
