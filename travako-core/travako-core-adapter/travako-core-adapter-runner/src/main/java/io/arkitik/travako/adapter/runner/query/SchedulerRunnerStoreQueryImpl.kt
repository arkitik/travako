package io.arkitik.travako.adapter.runner.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
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

    override fun findByServerAndRunnerKeyWithHost(server: ServerDomain, runnerKey: String, runnerHost: String) =
        travakoSchedulerRunnerRepository.findByServerAndRunnerKeyAndRunnerHost(
            server = server as TravakoServer,
            runnerKey = runnerKey,
            runnerHost = runnerHost,
        )

    override fun existsServerAndRunnerByKeyWithHost(server: ServerDomain, runnerKey: String, runnerHost: String) =
        travakoSchedulerRunnerRepository.existsByServerAndRunnerKeyAndRunnerHost(
            runnerKey = runnerKey,
            server = server as TravakoServer,
            runnerHost = runnerHost,
        )

    override fun existsServerAndRunnerByKeyAndHostAndStatus(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
        status: InstanceState,
    ) = travakoSchedulerRunnerRepository.existsByServerAndRunnerKeyAndRunnerHostAndInstanceState(
        server = server as TravakoServer,
        runnerKey = runnerKey,
        runnerHost = runnerHost,
        instanceState = status
    )

    override fun findServerOldestRunnerByHeartbeatAndStatus(server: ServerDomain, status: InstanceState) =
        travakoSchedulerRunnerRepository.findTopByServerAndInstanceStateOrderByLastHeartbeatTimeAsc(
            server = server as TravakoServer,
            instanceState = status
        )

    override fun findAllByServer(
        server: ServerDomain,
    ) = travakoSchedulerRunnerRepository.findAllByServer(
        server = server as TravakoServer
    )

    override fun findAllByServerAndStatus(server: ServerDomain, status: InstanceState) =
        travakoSchedulerRunnerRepository.findAllByServerAndInstanceState(server, status)
}
