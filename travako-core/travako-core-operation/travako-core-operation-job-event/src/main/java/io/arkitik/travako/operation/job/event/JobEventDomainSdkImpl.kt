package io.arkitik.travako.operation.job.event

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.job.event.operators.CleanupDoneJobEventsOperator
import io.arkitik.travako.operation.job.event.operators.CleanupJobEventStatesForDownRunnersOperator
import io.arkitik.travako.sdk.domain.job.event.JobEventDomainSdk
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore

/**
 * @author Ibrahim Al-Tamimi 
 * @since 16:59, Tuesday, 31/03/2026
 **/
class JobEventDomainSdkImpl(
    runnerJobEventStateStore: RunnerJobEventStateStore,
    jobEventStore: JobEventStore,
    travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : JobEventDomainSdk {
    override val cleanupDoneJobEvents: Operator<ServerDomain, Unit> =
        CleanupDoneJobEventsOperator(
            runnerJobEventStateStore = runnerJobEventStateStore,
            jobEventStore = jobEventStore,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
        )
    override val cleanupJobEventStatesForDownRunners: Operator<ServerDomain, Unit> =
        CleanupJobEventStatesForDownRunnersOperator(
            runnerJobEventStateStore = runnerJobEventStateStore,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
        )
}