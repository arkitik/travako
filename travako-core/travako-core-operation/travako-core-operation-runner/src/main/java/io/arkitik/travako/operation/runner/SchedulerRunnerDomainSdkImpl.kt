package io.arkitik.travako.operation.runner

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.runner.operation.FetchOldestHeartbeatRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchSchedulerRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchServerSchedulerRunnerOperationProvider
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
    override val fetchSchedulerRunner =
        FetchSchedulerRunnerOperationProvider(schedulerRunnerStore.storeQuery).fetchSchedulerRunner
    override val fetchServerSchedulerRunners =
        FetchServerSchedulerRunnerOperationProvider(schedulerRunnerStore.storeQuery).fetchServerSchedulerRunners
    override val fetchOldestHeartbeatRunner =
        FetchOldestHeartbeatRunnerOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
        ).fetchOldestHeartbeatRunner
}
