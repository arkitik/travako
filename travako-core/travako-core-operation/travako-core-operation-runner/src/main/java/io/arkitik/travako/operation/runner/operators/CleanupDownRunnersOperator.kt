package io.arkitik.travako.operation.runner.operators

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.store.runner.SchedulerRunnerStore

/**
 * @author Ibrahim Al-Tamimi 
 * @since 12:57, Tuesday, 31/03/2026
 **/
internal class CleanupDownRunnersOperator(
    private val schedulerRunnerStore: SchedulerRunnerStore,
) : Operator<ServerDomain, Unit> {
    override fun ServerDomain.operate(response: Unit) {
        schedulerRunnerStore.deleteAllByServerAndStatus(
            server = this,
            status = InstanceState.DOWN
        )
    }
}