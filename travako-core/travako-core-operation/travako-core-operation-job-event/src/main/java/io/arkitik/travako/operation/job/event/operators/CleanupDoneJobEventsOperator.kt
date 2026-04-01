package io.arkitik.travako.operation.job.event.operators

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * @author Ibrahim Al-Tamimi 
 * @since 16:59, Tuesday, 31/03/2026
 **/
internal class CleanupDoneJobEventsOperator(
    private val runnerJobEventStateStore: RunnerJobEventStateStore,
    private val jobEventStore: JobEventStore,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Operator<ServerDomain, Unit> {
    override fun ServerDomain.operate(response: Unit) {
        travakoTransactionalExecutor.runUnitTransaction {
            runnerJobEventStateStore.deleteAllByServerAndEventIsProcessed(this)
            jobEventStore.deleteAllByServerAndProcessed(this)
        }
    }
}