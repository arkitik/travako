package io.arkitik.travako.store.runner.query

import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:14 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface SchedulerRunnerStoreQuery : StoreQuery<String, SchedulerRunnerDomain> {
    fun findByRunnerKeyAndServerKey(
        runnerKey: String,
        serverKey: String,
    ): SchedulerRunnerDomain?

    fun existsRunnerByKeyAndServerKey(
        runnerKey: String,
        serverKey: String,
    ): Boolean

    fun existsRunnerByKeyAndServerKeyAndStatus(
        runnerKey: String,
        serverKey: String,
        status: InstanceState,
    ): Boolean

    fun findOldestRunnerByHeartbeatAndStatus(
        serverKey: String,
        status: InstanceState,
    ): SchedulerRunnerDomain?

    fun findAllByServerKey(
        serverKey: String,
    ): List<SchedulerRunnerDomain>
}
