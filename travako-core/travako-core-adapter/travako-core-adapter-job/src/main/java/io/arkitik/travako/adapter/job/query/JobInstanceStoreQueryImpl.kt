package io.arkitik.travako.adapter.job.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreQueryImpl(
    private val travakoJobInstanceRepository: TravakoJobInstanceRepository,
) : StoreQueryImpl<String, JobInstanceDomain, TravakoJobInstance>(travakoJobInstanceRepository),
    JobInstanceStoreQuery {

    override fun findAllByServerAndStatusIn(server: ServerDomain, statuses: List<JobStatus>) =
        travakoJobInstanceRepository.findAllByServerAndJobStatusIn(server, statuses)

    override fun findAllByServerAndRunner(server: ServerDomain, runner: SchedulerRunnerDomain) =
        travakoJobInstanceRepository.findAllByServerAndAssignedTo(
            server = server,
            runner = runner
        )

    override fun existsByServerAndJobKeyAndStatusIn(
        server: ServerDomain,
        jobKey: String,
        statuses: List<JobStatus>,
    ) =
        travakoJobInstanceRepository.existsByServerAndJobKeyAndJobStatusIn(
            server = server,
            jobKey = jobKey,
            jobStatus = statuses
        )

    override fun existsByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.existsByServerAndJobKeyIn(
            server = server,
            jobKeys = jobKeys
        )

    override fun findAllByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.findAllByServerAndJobKeyIn(
            server = server,
            jobKeys = jobKeys
        )

    override fun findByServerAndJobKey(server: ServerDomain, jobKey: String) =
        travakoJobInstanceRepository.findByServerAndJobKey(
            server = server,
            jobKey = jobKey
        )

    override fun existsByServerAndAssignedToRunnerAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ) = travakoJobInstanceRepository.existsByServerAndAssignedToAndJobKey(
        server = server,
        runner = runner,
        jobKey = jobKey
    )

    override fun findAllByServerAndRunnerAndStatusNextExecutionTimeIsBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        status: JobStatus,
        nextExecutionTime: LocalDateTime,
    ) = travakoJobInstanceRepository.findAllByNextExecutionTimeBeforeAndJobStatusAndServerAndAssignedTo(
        server = server,
        runner = runner,
        nextExecutionTime = nextExecutionTime,
        jobStatus = status,
    )

    override fun findByServerAndJobKeyAndStatus(
        server: ServerDomain,
        jobKey: String,
        status: JobStatus,
    ) = travakoJobInstanceRepository.findFirstByServerAndJobKeyAndJobStatus(
        server,
        jobKey,
        status
    )

}
