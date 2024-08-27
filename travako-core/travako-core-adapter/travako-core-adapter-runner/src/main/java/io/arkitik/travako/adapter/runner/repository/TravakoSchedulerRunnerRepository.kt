package io.arkitik.travako.adapter.runner.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:17 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface TravakoSchedulerRunnerRepository : RadixRepository<String, TravakoSchedulerRunner> {
    fun findByServerAndRunnerKeyAndRunnerHost(
        server: TravakoServer,
        runnerKey: String,
        runnerHost: String,
    ): TravakoSchedulerRunner?

    fun existsByServerAndRunnerKeyAndRunnerHost(
        server: TravakoServer,
        runnerKey: String,
        runnerHost: String,
    ): Boolean

    fun existsByServerAndRunnerKeyAndRunnerHostAndInstanceState(
        server: TravakoServer,
        runnerKey: String,
        runnerHost: String,
        instanceState: InstanceState,
    ): Boolean

    fun findTopByServerAndInstanceStateOrderByLastHeartbeatTimeAsc(
        server: TravakoServer,
        instanceState: InstanceState,
    ): TravakoSchedulerRunner?

    fun findAllByServer(
        server: TravakoServer,
    ): List<TravakoSchedulerRunner>

    fun findAllByServerAndInstanceState(
        server: ServerDomain,
        instanceState: InstanceState,
    ): List<TravakoSchedulerRunner>
}
