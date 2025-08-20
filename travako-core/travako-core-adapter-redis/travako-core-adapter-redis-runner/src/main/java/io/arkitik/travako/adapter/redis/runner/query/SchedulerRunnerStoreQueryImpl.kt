package io.arkitik.travako.adapter.redis.runner.query

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.travako.adapter.redis.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.redis.runner.TravakoSchedulerRunner
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

    override fun findByServerAndRunnerKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ) =
        travakoSchedulerRunnerRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).find {
            it.runnerKey == runnerKey && it.runnerHost == runnerHost
        }

    override fun existsServerAndRunnerByKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ) =
        travakoSchedulerRunnerRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).any {
            it.runnerKey == runnerKey && it.runnerHost == runnerHost
        }

    override fun existsServerAndRunnerByKeyAndHostAndStatus(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
        status: InstanceState,
    ) = travakoSchedulerRunnerRepository.findAllByServerUuid(
        serverUuid = server.uuid,
    ).any {
        it.runnerKey == runnerKey && it.runnerHost == runnerHost && it.instanceState == status
    }

    override fun findServerOldestRunnerByHeartbeatAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ) =
        travakoSchedulerRunnerRepository.findAllByServerUuid(
            serverUuid = server.uuid,
        ).filter { it.instanceState == status }
            .filter { it.lastHeartbeatTime != null }
            .minByOrNull { it.lastHeartbeatTime!! }

    override fun findAllByServer(
        server: ServerDomain,
    ) = travakoSchedulerRunnerRepository.findAllByServerUuid(
        serverUuid = server.uuid,
    )

    override fun findAllByServerAndStatus(server: ServerDomain, status: InstanceState) =
        travakoSchedulerRunnerRepository.findAllByServerUuid(server.uuid)
            .filter { it.instanceState == status }

    override fun countByServerAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ) = travakoSchedulerRunnerRepository.findAllByServerUuid(server.uuid)
        .count { it.instanceState == status }.toLong()
}
