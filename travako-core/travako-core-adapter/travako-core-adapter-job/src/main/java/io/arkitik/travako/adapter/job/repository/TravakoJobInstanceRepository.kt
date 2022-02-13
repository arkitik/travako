package io.arkitik.travako.adapter.job.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoJobInstanceRepository : RadixRepository<String, TravakoJobInstance> {
    fun findAllByServer(server: TravakoServer): List<TravakoJobInstance>

    fun findAllByServerAndAssignedTo(
        server: TravakoServer,
        runner: TravakoSchedulerRunner,
    ): List<TravakoJobInstance>

    fun existsByServerAndJobKey(server: TravakoServer, jobKey: String): Boolean

    fun existsAllByServerAndJobKeyIn(server: TravakoServer, jobKeys: List<String>): Boolean

    fun findAllByServerAndJobKeyIn(server: TravakoServer, jobKeys: List<String>): List<TravakoJobInstance>

    fun findByServerAndJobKey(server: TravakoServer, jobKey: String): TravakoJobInstance?

    fun existsByServerAndAssignedToAndJobKey(
        server: TravakoServer,
        runner: TravakoSchedulerRunner,
        jobKey: String,
    ): Boolean
}
