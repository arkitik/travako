package io.arkitik.travako.store.job.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceStoreQuery : StoreQuery<String, JobInstanceDomain> {
    fun findAllByServer(
        server: ServerDomain,
    ): List<JobInstanceDomain>

    fun findAllByServerAndRunner(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
    ): List<JobInstanceDomain>

    fun existsByServerAndJobKey(
        server: ServerDomain,
        jobKey: String,
    ): Boolean

    fun existsAllByServerAndJobKeys(
        server: ServerDomain,
        jobKeys: List<String>,
    ): Boolean

    fun findAllByServerAndJobKeys(
        server: ServerDomain,
        jobKeys: List<String>,
    ): List<JobInstanceDomain>

    fun findByServerAndJobKey(
        server: ServerDomain,
        jobKey: String,
    ): JobInstanceDomain?

    fun existsByServerAndAssignedToRunnerAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ): Boolean
}
