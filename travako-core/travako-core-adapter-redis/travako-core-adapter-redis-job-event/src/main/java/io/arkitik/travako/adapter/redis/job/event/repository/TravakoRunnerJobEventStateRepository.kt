package io.arkitik.travako.adapter.redis.job.event.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.redis.job.event.TravakoRunnerJobEventState

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:06 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoRunnerJobEventStateRepository : RadixRepository<String, TravakoRunnerJobEventState> {
    fun existsByJobEventUuidAndRunnerUuid(
        jobEventUuid: String,
        runnerUuid: String,
    ): Boolean

    fun findAllByJobEventUuid(jobEventUuid: String): List<TravakoRunnerJobEventState>
}
