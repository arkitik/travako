package io.arkitik.travako.adapter.redis.leader.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.redis.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.leader.TravakoLeader
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
    override fun findByServer(server: ServerDomain) =
        travakoLeaderRepository.findByServerUuid(server.uuid)

    override fun existsByServer(server: ServerDomain) =
        travakoLeaderRepository.existsByServerUuid(server.uuid)

    override fun existsByServerAndRunnerAndBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        beforeDate: LocalDateTime,
    ) = travakoLeaderRepository.findByServerUuid(
        serverUuid = server.uuid,
    )?.takeIf {
        runner.uuid == it.runnerUuid && it.lastModifiedDate.isBefore(beforeDate)
    } != null
}
