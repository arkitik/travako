package io.arkitik.travako.adapter.job.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.entity.job.TravakoJobInstance
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

    override fun findAllByServerKey(serverKey: String) =
        travakoJobInstanceRepository.findAllByServerServerKey(serverKey)

    override fun findAllByServerKeyAndRunnerKey(serverKey: String, runnerKey: String) =
        travakoJobInstanceRepository.findAllByServerServerKeyAndAssignedToRunnerKey(serverKey, runnerKey)

    override fun existsByServerKeyAndJobKey(serverKey: String, jobKey: String) =
        travakoJobInstanceRepository.existsByServerServerKeyAndJobKey(serverKey, jobKey)

    override fun existsAllByServerKeyAndJobKeys(serverKey: String, jobKeys: List<String>) =
        travakoJobInstanceRepository.existsAllByServerServerKeyAndJobKeyIn(serverKey, jobKeys)

    override fun findAllByServerKeyAndJobKeys(serverKey: String, jobKeys: List<String>) =
        travakoJobInstanceRepository.findAllByServerServerKeyAndJobKeyIn(serverKey, jobKeys)

    override fun findByServerKeyAndJobKey(serverKey: String, jobKey: String) =
        travakoJobInstanceRepository.findByServerServerKeyAndJobKey(serverKey, jobKey)

    override fun existsByServerKeyAndAssignedToRunnerKeyAndJobKey(
        serverKey: String,
        runnerKey: String,
        jobKey: String,
    ) = travakoJobInstanceRepository.existsByServerServerKeyAndAssignedToRunnerKeyAndJobKey(
        serverKey = serverKey, runnerKey = runnerKey, jobKey = jobKey
    )
}
