package io.arkitik.travako.adapter.redis.server.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.redis.server.TravakoServer

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:02 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoServerRepository : RadixRepository<String, TravakoServer> {
    fun findByServerKey(serverKey: String): TravakoServer?
    fun existsByServerKey(serverKey: String): Boolean
}
