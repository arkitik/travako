package io.arkitik.travako.store.runner.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerStoreQuery : StoreQuery<String, SchedulerRunnerDomain> {
    fun findByServerAndRunnerKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ): SchedulerRunnerDomain?

    fun existsServerAndRunnerByKeyWithHost(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
    ): Boolean

    fun existsServerAndRunnerByKeyAndHostAndStatus(
        server: ServerDomain,
        runnerKey: String,
        runnerHost: String,
        status: InstanceState,
    ): Boolean

    fun findServerOldestRunnerByHeartbeatAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): SchedulerRunnerDomain?

    fun findAllByServer(
        server: ServerDomain,
    ): List<SchedulerRunnerDomain>

    fun findAllByServerAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): List<SchedulerRunnerDomain>

    fun countByServerAndStatus(
        server: ServerDomain,
        status: InstanceState,
    ): Long
}
