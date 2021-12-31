package io.arkitik.travako.adapter.job.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.entity.job.TravakoJobInstance

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoJobInstanceRepository : RadixRepository<String, TravakoJobInstance> {
    fun findAllByServerServerKey(serverKey: String): List<TravakoJobInstance>

    fun findAllByServerServerKeyAndAssignedToRunnerKey(serverKey: String, runnerKey: String): List<TravakoJobInstance>

    fun existsByServerServerKeyAndJobKey(serverKey: String, jobKey: String): Boolean

    fun existsAllByServerServerKeyAndJobKeyIn(serverKey: String, jobKeys: List<String>): Boolean

    fun findAllByServerServerKeyAndJobKeyIn(serverKey: String, jobKeys: List<String>): List<TravakoJobInstance>

    fun findByServerServerKeyAndJobKey(serverKey: String, jobKey: String): TravakoJobInstance?

    fun existsByServerServerKeyAndAssignedToRunnerKeyAndJobKey(
        serverKey: String,
        runnerKey: String,
        jobKey: String,
    ): Boolean
}
