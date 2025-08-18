package io.arkitik.travako.adapter.exposed.leader.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.leader.TravakoLeaderTable
import io.arkitik.travako.store.leader.query.LeaderStoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:54 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderStoreQueryImpl(
    database: Database?,
    identityTable: TravakoLeaderTable,
) : ExposedStoreQuery<String, LeaderDomain, TravakoLeaderTable>(
    identityTable,
    database
), LeaderStoreQuery {
    override fun findByServer(server: ServerDomain): LeaderDomain? =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    identityTable.server eq server.uuid
                }.limit(1)
                .singleOrNull()?.let { identityTable.mapToIdentity(it, database) }
        }

    override fun existsByServer(server: ServerDomain): Boolean =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    identityTable.server eq server.uuid
                }.exist()
        }

    override fun existsByServerAndRunnerAndBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        beforeDate: LocalDateTime,
    ): Boolean = transaction(database) {
        identityTable.selectAll()
            .where {
                (identityTable.server eq server.uuid)
                    .and(identityTable.runner eq runner.uuid)
                    .and(identityTable.lastModifiedDate less beforeDate)
            }.exist()
    }
}
