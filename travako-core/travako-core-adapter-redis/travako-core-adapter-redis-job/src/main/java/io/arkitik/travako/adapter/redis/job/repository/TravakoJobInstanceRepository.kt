package io.arkitik.travako.adapter.redis.job.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.redis.job.TravakoJobInstance

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoJobInstanceRepository : RadixRepository<String, TravakoJobInstance> {
    fun findAllByServerUuid(
        serverUuid: String,
    ): List<TravakoJobInstance>
}
