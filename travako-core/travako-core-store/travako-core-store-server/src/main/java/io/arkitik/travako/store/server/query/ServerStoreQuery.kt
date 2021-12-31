package io.arkitik.travako.store.server.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:39 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerStoreQuery : StoreQuery<String, ServerDomain> {
    fun findByServerKey(
        serverKey: String,
    ): ServerDomain?

    fun existsByServerKey(
        serverKey: String,
    ): Boolean
}
