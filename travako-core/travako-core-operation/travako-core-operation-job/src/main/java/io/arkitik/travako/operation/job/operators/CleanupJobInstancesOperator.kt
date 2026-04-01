package io.arkitik.travako.operation.job.operators

import io.arkitik.radix.develop.operation.Operator
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.domain.job.dto.JobCleanupDto
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore
import java.time.LocalTime

/**
 * @author Ibrahim Al-Tamimi 
 * @since 11:25, Tuesday, 31/03/2026
 **/
internal class CleanupJobInstancesOperator(
    private val jobInstanceStore: JobInstanceStore,
    private val jobInstanceParamStore: JobInstanceParamStore,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Operator<JobCleanupDto, Unit> {
    override fun JobCleanupDto.operate(response: Unit) {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstanceParamStore.deleteAllParamsForJobsByLastRunningDateAndStatus(
                lastRunningTime = beforeDate.atTime(LocalTime.MAX),
                statuses = JobStatus.unrepeatable(),
                server = server
            )
            jobInstanceStore.deleteAllByLastRunningDateAndStatus(
                server = server,
                lastRunningTime = beforeDate.atTime(LocalTime.MAX),
                statuses = JobStatus.unrepeatable(),
            )
        }
    }
}