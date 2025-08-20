package io.arkitik.travako.adapter.exposed.runner

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.travako.adapter.exposed.runner.creator.SchedulerRunnerCreatorImpl
import io.arkitik.travako.adapter.exposed.runner.query.SchedulerRunnerStoreQueryImpl
import io.arkitik.travako.adapter.exposed.runner.updater.SchedulerRunnerUpdaterImpl
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import io.arkitik.travako.store.runner.creator.SchedulerRunnerCreator
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery
import io.arkitik.travako.store.runner.updater.SchedulerRunnerUpdater
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:15 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, SchedulerRunnerDomain, TravakoSchedulerRunnerTable>(
    identityTable = TravakoSchedulerRunnerTable(travakoExposedNamingStrategy),
    database = database,
), SchedulerRunnerStore {
    private fun SchedulerRunnerDomain.map() = this as TravakoSchedulerRunner

    override val storeQuery: SchedulerRunnerStoreQuery =
        SchedulerRunnerStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): SchedulerRunnerCreator =
        SchedulerRunnerCreatorImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun SchedulerRunnerDomain.identityUpdater(): SchedulerRunnerUpdater =
        SchedulerRunnerUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: SchedulerRunnerDomain) {
        this[identityTable.runnerKey] = identity.runnerKey
        this[identityTable.runnerHost] = identity.runnerHost
        this[identityTable.server] = identity.map().serverUuid
        this[identityTable.instanceState] = identity.instanceState
        this[identityTable.lastHeartbeatTime] = identity.lastHeartbeatTime
    }

    override fun <K : Any> UpdateBuilder<K>.updateEntity(identity: SchedulerRunnerDomain) {
        this[identityTable.instanceState] = identity.instanceState
        this[identityTable.lastHeartbeatTime] = identity.lastHeartbeatTime
    }
}
