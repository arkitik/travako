package io.arkitik.travako.adapter.server

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.travako.adapter.server.creator.ServerCreatorImpl
import io.arkitik.travako.adapter.server.query.ServerStoreQueryImpl
import io.arkitik.travako.adapter.server.repository.TravakoServerRepository
import io.arkitik.travako.adapter.server.updater.ServerUpdaterImpl
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.server.ServerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:01 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerStoreImpl(
    travakoServerRepository: TravakoServerRepository,
) : StoreImpl<String, ServerDomain, TravakoServer>(travakoServerRepository), ServerStore {

    override fun ServerDomain.map() = this as TravakoServer

    override val storeQuery = ServerStoreQueryImpl(travakoServerRepository)

    override fun identityCreator() =
        ServerCreatorImpl()

    override fun ServerDomain.identityUpdater() =
        ServerUpdaterImpl(map())
}
