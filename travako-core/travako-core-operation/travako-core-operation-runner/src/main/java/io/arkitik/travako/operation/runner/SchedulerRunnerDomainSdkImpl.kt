package io.arkitik.travako.operation.runner

import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.operation.runner.operation.FetchOldestHeartbeatRunnerOperationProvider
import io.arkitik.travako.operation.runner.operation.FetchSchedulerRunnerOperationProvider
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:54 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class SchedulerRunnerDomainSdkImpl(
    schedulerRunnerStore: SchedulerRunnerStore,
    transactionalExecutor: TransactionalExecutor,
) : SchedulerRunnerDomainSdk {
    override val fetchSchedulerRunner =
        FetchSchedulerRunnerOperationProvider(schedulerRunnerStore.storeQuery).fetchSchedulerRunner
    override val fetchOldestHeartbeatRunner =
        FetchOldestHeartbeatRunnerOperationProvider(
            schedulerRunnerStoreQuery = schedulerRunnerStore.storeQuery,
            transactionalExecutor = transactionalExecutor,
        ).fetchOldestHeartbeatRunner
}
