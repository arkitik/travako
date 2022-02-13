package io.arkitik.travako.adapter.leader.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
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
        travakoLeaderRepository.findByServer(server as TravakoServer)

    override fun existsByServer(server: ServerDomain) =
        travakoLeaderRepository.existsByServer(server as TravakoServer)

    override fun existsByServerAndRunnerAndBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        beforeDate: LocalDateTime,
    ) = travakoLeaderRepository.existsByServerAndRunnerAndLastModifiedDateBefore(
        server = server as TravakoServer,
        runner = runner as TravakoSchedulerRunner,
        lastModifiedDate = beforeDate,
    )
}
