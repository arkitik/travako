package io.arkitik.travako.store.job.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.job.JobInstanceDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceStoreQuery : StoreQuery<String, JobInstanceDomain> {
    fun findAllByServerKey(
        serverKey: String,
    ): List<JobInstanceDomain>

    fun findAllByServerKeyAndRunnerKey(
        serverKey: String,
        runnerKey: String,
    ): List<JobInstanceDomain>

    fun existsByServerKeyAndJobKey(
        serverKey: String,
        jobKey: String,
    ): Boolean

    fun existsAllByServerKeyAndJobKeys(
        serverKey: String,
        jobKeys: List<String>,
    ): Boolean

    fun findAllByServerKeyAndJobKeys(
        serverKey: String,
        jobKeys: List<String>,
    ): List<JobInstanceDomain>

    fun findByServerKeyAndJobKey(
        serverKey: String,
        jobKey: String,
    ): JobInstanceDomain?

    fun existsByServerKeyAndAssignedToRunnerKeyAndJobKey(
        serverKey: String,
        runnerKey: String,
        jobKey: String,
    ): Boolean
}
