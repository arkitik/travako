package io.arkitik.travako.store.server

import io.arkitik.radix.develop.store.Store
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.store.server.creator.ServerCreator
import io.arkitik.travako.store.server.query.ServerStoreQuery
import io.arkitik.travako.store.server.updater.ServerUpdater

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:38 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerStore : Store<String, ServerDomain> {
    override val storeQuery: ServerStoreQuery

    override fun identityCreator(): ServerCreator
    override fun ServerDomain.identityUpdater(): ServerUpdater
}
