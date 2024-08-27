package io.arkitik.travako.store.job.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface JobInstanceStoreQuery : StoreQuery<String, JobInstanceDomain> {
    fun findAllByServerAndStatusIn(
        server: ServerDomain,
        statuses: List<JobStatus>,
    ): List<JobInstanceDomain>

    fun findAllByServerAndRunner(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
    ): List<JobInstanceDomain>

    fun existsByServerAndJobKeyAndStatusIn(
        server: ServerDomain,
        jobKey: String,
        statuses: List<JobStatus>,
    ): Boolean

    fun existsByServerAndJobKeys(
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

    fun findAllByServerAndRunnerAndStatusNextExecutionTimeIsBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        status: JobStatus,
        nextExecutionTime: LocalDateTime,
    ): List<JobInstanceDomain>


    fun findByServerAndJobKeyAndStatus(
        server: ServerDomain,
        jobKey: String,
        status: JobStatus,
    ): JobInstanceDomain?
}
