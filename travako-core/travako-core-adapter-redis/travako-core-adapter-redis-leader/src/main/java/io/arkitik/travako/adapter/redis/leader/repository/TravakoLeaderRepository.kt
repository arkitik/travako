package io.arkitik.travako.adapter.redis.leader.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.redis.leader.TravakoLeader

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:52 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoLeaderRepository : RadixRepository<String, TravakoLeader> {
    fun findByServerUuid(serverUuid: String): TravakoLeader?
    fun existsByServerUuid(serverUuid: String): Boolean
}
