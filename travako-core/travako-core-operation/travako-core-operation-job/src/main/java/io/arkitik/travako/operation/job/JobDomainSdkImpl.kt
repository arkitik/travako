package io.arkitik.travako.operation.job

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.job.operation.FetchJobInstanceOperationProvider
import io.arkitik.travako.operation.job.operators.CleanupJobInstancesOperator
import io.arkitik.travako.operation.job.operators.RemoveAssigneeFromDownRunnersOperator
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.dto.JobCleanupDto
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:49 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobDomainSdkImpl(
    jobInstanceStore: JobInstanceStore,
    jobInstanceParamStore: JobInstanceParamStore,
    travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : JobDomainSdk {
    override val fetchJobInstance = FetchJobInstanceOperationProvider(
        jobInstanceStoreQuery = jobInstanceStore.storeQuery,
    ).fetchJobInstance
    override val cleanupDoneJobInstances: Operator<JobCleanupDto, Unit> =
        CleanupJobInstancesOperator(
            jobInstanceStore = jobInstanceStore,
            jobInstanceParamStore = jobInstanceParamStore,
            travakoTransactionalExecutor = travakoTransactionalExecutor
        )

    override val removeAssigneeFromDownRunners: Operator<ServerDomain, Unit> =
        RemoveAssigneeFromDownRunnersOperator(
            jobInstanceStore = jobInstanceStore,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
        )
}
