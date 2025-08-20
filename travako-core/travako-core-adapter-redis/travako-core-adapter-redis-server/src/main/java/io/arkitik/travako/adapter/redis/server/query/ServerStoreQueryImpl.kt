package io.arkitik.travako.adapter.redis.server.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.redis.server.repository.TravakoServerRepository
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.server.TravakoServer
import io.arkitik.travako.store.server.query.ServerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:03 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerStoreQueryImpl(
    private val travakoServerRepository: TravakoServerRepository,
) : StoreQueryImpl<String, ServerDomain, TravakoServer>(travakoServerRepository), ServerStoreQuery {
    override fun findByServerKey(
        serverKey: String,
    ) = travakoServerRepository.findByServerKey(serverKey)

    override fun existsByServerKey(
        serverKey: String,
    ) = travakoServerRepository.existsByServerKey(serverKey)
}
