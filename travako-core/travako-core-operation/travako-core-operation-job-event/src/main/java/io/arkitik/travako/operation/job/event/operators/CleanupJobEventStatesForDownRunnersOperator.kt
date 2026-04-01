package io.arkitik.travako.operation.job.event.operators

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * @author Ibrahim Al-Tamimi 
 * @since 17:32, Tuesday, 31/03/2026
 **/
internal class CleanupJobEventStatesForDownRunnersOperator(
    private val runnerJobEventStateStore: RunnerJobEventStateStore,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Operator<ServerDomain, Unit> {
    override fun ServerDomain.operate(response: Unit) {
        travakoTransactionalExecutor.runUnitTransaction {
            runnerJobEventStateStore.deleteAllByServerAndRunnerStatus(
                server = this,
                runnerStatus = InstanceState.DOWN
            )
        }
    }
}