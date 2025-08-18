package io.arkitik.travako.adapter.exposed.runner.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerStoreQueryImpl(
    database: Database?,
    identityTable: TravakoSchedulerRunnerTable,
) : ExposedStoreQuery<String, SchedulerRunnerDomain, TravakoSchedulerRunnerTable>(
    identityTable = identityTable,
    database = database
), SchedulerRunnerStoreQuery {

    override fun findAllByServer(server: ServerDomain): List<SchedulerRunnerDomain> {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    identityTable.server eq server.uuid
                }.map { identityTable.mapToIdentity(it, database) }
        }
    }

    override fun findAllByServerAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): List<SchedulerRunnerDomain> {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server eq server.uuid) and
                            (identityTable.instanceState eq status)
                }.map { identityTable.mapToIdentity(it, database) }
        }
    }

    override fun countByServerAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): Long {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server eq server.uuid) and
                            (identityTable.instanceState eq status)
                }.count()
        }
    }

    override fun findByServerAndRunnerKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ): SchedulerRunnerDomain? =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server eq server.uuid) and
                            (identityTable.runnerKey eq runnerKey) and
                            (identityTable.runnerHost eq runnerHost)
                }.map { identityTable.mapToIdentity(it, database) }.firstOrNull()
        }

    override fun existsServerAndRunnerByKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ): Boolean =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server eq server.uuid) and
                            (identityTable.runnerKey eq runnerKey) and
                            (identityTable.runnerHost eq runnerHost)
                }.exist()
        }

    override fun existsServerAndRunnerByKeyAndHostAndStatus(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
        status: InstanceState,
    ): Boolean = transaction(database) {
        identityTable.selectAll()
            .where {
                (identityTable.server eq server.uuid) and
                        (identityTable.runnerKey eq runnerKey) and
                        (identityTable.runnerHost eq runnerHost) and
                        (identityTable.instanceState eq status)
            }.any()
    }

    override fun findServerOldestRunnerByHeartbeatAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): SchedulerRunnerDomain? =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.server eq server.uuid) and
                            (identityTable.instanceState eq status)
                }.orderBy(identityTable.lastHeartbeatTime)
                .limit(1)
                .singleOrNull()?.let { identityTable.mapToIdentity(it, database) }
        }
}
