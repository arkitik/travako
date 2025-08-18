package io.arkitik.travako.adapter.exposed.leader

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.travako.adapter.exposed.leader.creator.LeaderCreatorImpl
import io.arkitik.travako.adapter.exposed.leader.query.LeaderStoreQueryImpl
import io.arkitik.travako.adapter.exposed.leader.updater.LeaderUpdaterImpl
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.exposed.leader.TravakoLeader
import io.arkitik.travako.entity.exposed.leader.TravakoLeaderTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.leader.LeaderStore
import io.arkitik.travako.store.leader.creator.LeaderCreator
import io.arkitik.travako.store.leader.query.LeaderStoreQuery
import io.arkitik.travako.store.leader.updater.LeaderUpdater
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:50 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, LeaderDomain, TravakoLeaderTable>(
    TravakoLeaderTable(travakoExposedNamingStrategy)
), LeaderStore {
    private fun LeaderDomain.map() = this as TravakoLeader

    override val storeQuery: LeaderStoreQuery =
        LeaderStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): LeaderCreator =
        LeaderCreatorImpl(
            identityTable = identityTable,
            database = database
        )

    override fun LeaderDomain.identityUpdater(): LeaderUpdater =
        LeaderUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: LeaderDomain) {
        this[identityTable.server] = identity.map().serverUuid
        this[identityTable.runner] = identity.map().runnerUuid
        this[identityTable.lastModifiedDate] = identity.lastModifiedDate
    }

    override fun <K : Any> UpdateBuilder<K>.updateEntity(identity: LeaderDomain) {
        this[identityTable.runner] = identity.map().runnerUuid
        this[identityTable.lastModifiedDate] = identity.lastModifiedDate
    }
}
