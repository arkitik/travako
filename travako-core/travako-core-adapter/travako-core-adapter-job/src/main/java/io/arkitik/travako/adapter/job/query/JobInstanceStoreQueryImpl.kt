package io.arkitik.travako.adapter.job.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreQueryImpl(
    private val travakoJobInstanceRepository: TravakoJobInstanceRepository,
) : StoreQueryImpl<String, JobInstanceDomain, TravakoJobInstance>(travakoJobInstanceRepository),
    JobInstanceStoreQuery {

    override fun findAllByServer(server: ServerDomain) =
        travakoJobInstanceRepository.findAllByServer(server as TravakoServer)

    override fun findAllByServerAndRunner(server: ServerDomain, runner: SchedulerRunnerDomain) =
        travakoJobInstanceRepository.findAllByServerAndAssignedTo(
            server = server as TravakoServer,
            runner = runner as TravakoSchedulerRunner
        )

    override fun existsByServerAndJobKey(server: ServerDomain, jobKey: String) =
        travakoJobInstanceRepository.existsByServerAndJobKey(
            server = server as TravakoServer,
            jobKey = jobKey
        )

    override fun existsAllByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.existsAllByServerAndJobKeyIn(
            server = server as TravakoServer,
            jobKeys = jobKeys
        )

    override fun findAllByServerAndJobKeys(server: ServerDomain, jobKeys: List<String>) =
        travakoJobInstanceRepository.findAllByServerAndJobKeyIn(
            server = server as TravakoServer,
            jobKeys = jobKeys
        )

    override fun findByServerAndJobKey(server: ServerDomain, jobKey: String) =
        travakoJobInstanceRepository.findByServerAndJobKey(
            server = server as TravakoServer,
            jobKey = jobKey
        )

    override fun existsByServerAndAssignedToRunnerAndJobKey(
        server: ServerDomain,
        runner: SchedulerRunnerDomain,
        jobKey: String,
    ) = travakoJobInstanceRepository.existsByServerAndAssignedToAndJobKey(
        server = server as TravakoServer,
        runner = runner as TravakoSchedulerRunner,
        jobKey = jobKey
    )
}
