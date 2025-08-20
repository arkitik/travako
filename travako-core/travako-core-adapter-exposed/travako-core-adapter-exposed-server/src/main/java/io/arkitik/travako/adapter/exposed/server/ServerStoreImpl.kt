package io.arkitik.travako.adapter.exposed.server

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.travako.adapter.exposed.server.creator.ServerCreatorImpl
import io.arkitik.travako.adapter.exposed.server.query.ServerStoreQueryImpl
import io.arkitik.travako.adapter.exposed.server.updater.ServerUpdaterImpl
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.server.TravakoServer
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.server.ServerStore
import io.arkitik.travako.store.server.creator.ServerCreator
import io.arkitik.travako.store.server.query.ServerStoreQuery
import io.arkitik.travako.store.server.updater.ServerUpdater
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:01 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, ServerDomain, TravakoServerTable>(
    TravakoServerTable(travakoExposedNamingStrategy)
), ServerStore {

    private fun ServerDomain.map() = this as TravakoServer

    override val storeQuery: ServerStoreQuery =
        ServerStoreQueryImpl(
            database = database,
            travakoServerTable = identityTable,
        )

    override fun identityCreator(): ServerCreator =
        ServerCreatorImpl()

    override fun ServerDomain.identityUpdater(): ServerUpdater =
        ServerUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: ServerDomain) {
        this[identityTable.serverKey] = identity.serverKey
    }
}
