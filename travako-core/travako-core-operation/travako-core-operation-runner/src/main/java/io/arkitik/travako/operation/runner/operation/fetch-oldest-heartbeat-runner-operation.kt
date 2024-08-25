package io.arkitik.travako.operation.runner.operation

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.notAcceptable
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.runner.errors.RunnerErrors
import io.arkitik.travako.store.runner.query.SchedulerRunnerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:19 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchOldestHeartbeatRunnerOperationProvider(
    private val schedulerRunnerStoreQuery: SchedulerRunnerStoreQuery,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) {
    val fetchOldestHeartbeatRunner: Operation<ServerDomain, SchedulerRunnerDomain> =
        operationBuilder {
            mainOperation {
                travakoTransactionalExecutor.runOnTransaction {
                    with(schedulerRunnerStoreQuery) {
                        findServerOldestRunnerByHeartbeatAndStatus(
                            server = this@mainOperation,
                            status = InstanceState.UP
                        ) ?: throw RunnerErrors.NO_REGISTERED_RUNNER.notAcceptable()
                    }
                } ?: throw RunnerErrors.NO_REGISTERED_RUNNER.notAcceptable()
            }
        }
}
