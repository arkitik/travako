package io.arkitik.travako.protocol.job

import io.arkitik.travako.protocol.job.dto.TravakoJobExecutionData
import io.arkitik.travako.protocol.job.dto.TravakoJobExecutionResult

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:52 PM, 26/08/2024
 */
interface TravakoJob : StatefulTravakoJob {
    override fun executeJob(executionData: TravakoJobExecutionData): TravakoJobExecutionResult {
        return runCatching {
            execute(executionData)
            TravakoJobExecutionResult.success()
        }.getOrElse { throwable ->
            TravakoJobExecutionResult.failure(throwable)
        }
    }

    fun execute(executionData: TravakoJobExecutionData)
}
