package io.arkitik.travako.adapter.runner.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerStoreQueryImpl(
    private val travakoSchedulerRunnerRepository: TravakoSchedulerRunnerRepository,
) : StoreQueryImpl<String, SchedulerRunnerDomain, TravakoSchedulerRunner>(travakoSchedulerRunnerRepository),
    SchedulerRunnerStoreQuery {

    override fun findByRunnerKeyAndServerKey(runnerKey: String, serverKey: String) =
        travakoSchedulerRunnerRepository.findByRunnerKeyAndServerServerKey(
            runnerKey = runnerKey,
            serverKey = serverKey
        )

    override fun existsRunnerByKeyAndServerKey(runnerKey: String, serverKey: String) =
        travakoSchedulerRunnerRepository.existsByRunnerKeyAndServerServerKey(
            runnerKey = runnerKey,
            serverKey = serverKey
        )

    override fun existsRunnerByKeyAndServerKeyAndStatus(runnerKey: String, serverKey: String, status: InstanceState) =
        travakoSchedulerRunnerRepository.existsByRunnerKeyAndServerServerKeyAndInstanceState(
            runnerKey = runnerKey,
            serverKey = serverKey,
            instanceState = status
        )

    override fun findOldestRunnerByHeartbeatAndStatus(serverKey: String, status: InstanceState) =
        travakoSchedulerRunnerRepository.findTopByServerServerKeyAndInstanceStateOrderByLastHeartbeatTimeAsc(
            serverKey = serverKey,
            instanceState = status
        )

    override fun findAllByServerKey(
        serverKey: String,
    ) = travakoSchedulerRunnerRepository.findAllByServerServerKey(
        serverKey
    )
}
