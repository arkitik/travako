package io.arkitik.travako.operation.runner

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.runner.operation.FetchOldestHeartbeatRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchSchedulerRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchServerSchedulerRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchServerSchedulerRunningRunnersOperationProvider
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:54 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerDomainSdkImpl(
    schedulerRunnerStore: SchedulerRunnerStore,
    travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : SchedulerRunnerDomainSdk {
    private val runningRunnersOperationProvider = FetchServerSchedulerRunningRunnersOperationProvider(
        schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery
    )
    override val fetchSchedulerRunner =
        FetchSchedulerRunnerOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery
        ).fetchSchedulerRunner

    override val fetchServerSchedulerRunners =
        FetchServerSchedulerRunnerOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery
        ).fetchServerSchedulerRunners
    override val fetchOldestHeartbeatRunner =
        FetchOldestHeartbeatRunnerOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
        ).fetchOldestHeartbeatRunner

    override val fetchServerSchedulerRunningRunners: Operation<ServerDomain, List<SchedulerRunnerDomain>> =
        runningRunnersOperationProvider.fetchServerSchedulerRunningRunners
    override val countServerSchedulerRunningRunners: Operation<ServerDomain, Long> =
        runningRunnersOperationProvider.countServerSchedulerRunningRunners
}
