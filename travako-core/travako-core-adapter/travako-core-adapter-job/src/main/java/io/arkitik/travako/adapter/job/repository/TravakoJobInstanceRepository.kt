package io.arkitik.travako.adapter.job.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoJobInstanceRepository : RadixRepository<String, TravakoJobInstance> {
    fun findAllByServerAndJobStatusIn(server: ServerDomain, jobStatus: List<JobStatus>): List<TravakoJobInstance>

    fun findAllByServerAndAssignedTo(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
    ): List<TravakoJobInstance>

    fun existsByServerAndJobKeyAndJobStatusIn(
        server: ServerDomain,
        jobKey: String,
        jobStatus: List<JobStatus>,
    ): Boolean

    fun existsByServerAndJobKeyIn(server: ServerDomain, jobKeys: List<String>): Boolean

    fun findAllByServerAndJobKeyIn(server: ServerDomain, jobKeys: List<String>): List<TravakoJobInstance>

    fun findByServerAndJobKey(server: ServerDomain, jobKey: String): TravakoJobInstance?

    fun existsByServerAndAssignedToAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ): Boolean

    fun findAllByNextExecutionTimeBeforeAndJobStatusAndServerAndAssignedTo(
        nextExecutionTime: LocalDateTime,
        jobStatus: JobStatus,
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        pageable: Pageable,
    ): List<TravakoJobInstance>

    fun findFirstByServerAndJobKeyAndJobStatus(
        server: ServerDomain,
        jobKey: String,
        jobStatus: JobStatus,
    ): JobInstanceDomain?
}
