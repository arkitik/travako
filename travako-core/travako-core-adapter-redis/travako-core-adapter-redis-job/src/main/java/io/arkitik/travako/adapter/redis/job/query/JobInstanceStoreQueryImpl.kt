package io.arkitik.travako.adapter.redis.job.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.redis.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.job.TravakoJobInstance
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstanceStoreQueryImpl(
    private val travakoJobInstanceRepository: TravakoJobInstanceRepository,
) : StoreQueryImpl<String, JobInstanceDomain, TravakoJobInstance>(travakoJobInstanceRepository),
    JobInstanceStoreQuery {

    override fun findAllByServerAndStatusIn(server: ServerDomain, statuses: List<JobStatus>) =
        travakoJobInstanceRepository.findAllByServerUuid(server.uuid)
            .filter { statuses.contains(it.jobStatus) }

    override fun findAllByServerAndRunner(server: ServerDomain, runner: SchedulerRunnerDomain) =
        travakoJobInstanceRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).filter { it.assignedToUuid == runner.uuid }

    override fun existsByServerAndJobKeyAndStatusIn(
        server: ServerDomain,
        jobKey: String,
        statuses: List<JobStatus>,
    ) =
        travakoJobInstanceRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).any { statuses.contains(it.jobStatus) && it.jobKey == jobKey }

    override fun existsByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.findAllByServerUuid(server.uuid)
            .any { jobKeys.contains(it.jobKey) }

    override fun findAllByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.findAllByServerUuid(server.uuid)
            .filter { jobKeys.contains(it.jobKey) }

    override fun findByServerAndJobKey(server: ServerDomain, jobKey: String) =
        travakoJobInstanceRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).find { it.jobKey == jobKey }

    override fun existsByServerAndAssignedToRunnerAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ) = travakoJobInstanceRepository.findAllByServerUuid(
        serverUuid = server.uuid,
    ).any { it.assignedToUuid == runner.uuid && it.jobKey == jobKey }

    override fun findAllByServerAndRunnerAndStatusNextExecutionTimeIsBefore(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        status: JobStatus,
        nextExecutionTime: LocalDateTime,
    ) = travakoJobInstanceRepository.findAllByServerUuid(
        serverUuid = server.uuid,
    ).filter {
        it.jobStatus == status && it.assignedToUuid == runner.uuid
                && (it.nextExecutionTime?.isBefore(nextExecutionTime) ?: false)
    }

    override fun findByServerAndJobKeyAndStatus(
        server: ServerDomain,
        jobKey: String,
        status: JobStatus,
    ) = travakoJobInstanceRepository.findAllByServerUuid(
        serverUuid = server.uuid
    ).find { it.jobStatus == status && it.jobKey == jobKey }

}
