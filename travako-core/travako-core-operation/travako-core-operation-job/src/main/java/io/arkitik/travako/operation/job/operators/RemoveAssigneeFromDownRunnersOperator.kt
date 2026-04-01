package io.arkitik.travako.operation.job.operators

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.radix.develop.store.storeUpdater
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * @author Ibrahim Al-Tamimi 
 * @since 18:13, Tuesday, 31/03/2026
 **/
internal class RemoveAssigneeFromDownRunnersOperator(
    private val jobInstanceStore: JobInstanceStore,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Operator<ServerDomain, Unit> {
    override fun ServerDomain.operate(response: Unit) {
        with(jobInstanceStore) {
            travakoTransactionalExecutor.runUnitTransaction {
                val instanceDomains =
                    storeQuery.findAllByServerAndRunnerStateIs(this@operate, InstanceState.DOWN)
                instanceDomains.map {
                    storeUpdater(it.identityUpdater()) {
                        removeRunnerAssignee()
                        update()
                    }
                }.updateIgnore()
            }
        }
    }
}