package io.arkitik.travako.adapter.exposed.job.event

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.adapter.exposed.job.event.creator.RunnerJobEventStateCreatorImpl
import io.arkitik.travako.adapter.exposed.job.event.query.RunnerJobEventStateStoreQueryImpl
import io.arkitik.travako.adapter.exposed.job.event.updater.RunnerJobEventStateUpdaterImpl
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventStateTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore
import io.arkitik.travako.store.job.event.creator.RunnerJobEventStateCreator
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery
import io.arkitik.travako.store.job.event.updater.RunnerJobEventStateUpdater
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.exists
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.stringLiteral
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:05 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobEventStateStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, RunnerJobEventStateDomain, TravakoRunnerJobEventStateTable>(
    identityTable = TravakoRunnerJobEventStateTable(travakoExposedNamingStrategy),
    database = database
), RunnerJobEventStateStore {
    private fun RunnerJobEventStateDomain.map() = this as TravakoRunnerJobEventState

    override val storeQuery: RunnerJobEventStateStoreQuery =
        RunnerJobEventStateStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): RunnerJobEventStateCreator =
        RunnerJobEventStateCreatorImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun RunnerJobEventStateDomain.identityUpdater(): RunnerJobEventStateUpdater =
        RunnerJobEventStateUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: RunnerJobEventStateDomain) {
        this[identityTable.runner] = identity.map().runnerUuid
        this[identityTable.jobEvent] = identity.map().jobEventUuid
    }

    override fun deleteAllByServerAndEventIsProcessed(server: ServerDomain) {
        ensureInTransaction(database) {
            val jobEventTable = identityTable.jobEventTable
            val jobInstanceTable = jobEventTable.jobInstanceTable
            identityTable.deleteWhere {
                exists(
                    jobEventTable
                        .innerJoin(jobInstanceTable)
                        .select(stringLiteral("1"))
                        .where {
                            jobInstanceTable.server.eq(server.uuid)
                                .and(jobEventTable.processedFlag.eq(true))
                                .and(identityTable.jobEvent.eq(jobEventTable.uuid))
                        }
                )
            }
        }
    }

    override fun deleteAllByServerAndRunnerStatus(
        server: ServerDomain,
        runnerStatus: InstanceState,
    ) {
        ensureInTransaction(database) {
            val runnerTable = identityTable.runnerTable
            identityTable.deleteWhere {
                exists(
                    runnerTable
                        .select(stringLiteral("1"))
                        .where {
                            runnerTable.server.eq(server.uuid)
                                .and(identityTable.runner.eq(runnerTable.uuid))
                                .and(runnerTable.instanceState.eq(runnerStatus))
                        }
                )
            }
        }
    }
}
