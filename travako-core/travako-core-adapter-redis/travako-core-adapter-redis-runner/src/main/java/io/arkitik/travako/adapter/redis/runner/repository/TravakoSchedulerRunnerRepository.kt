package io.arkitik.travako.adapter.redis.runner.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoSchedulerRunnerRepository : RadixRepository<String, TravakoSchedulerRunner> {
    fun findAllByServerUuid(serverUuid: String): List<TravakoSchedulerRunner>
}
