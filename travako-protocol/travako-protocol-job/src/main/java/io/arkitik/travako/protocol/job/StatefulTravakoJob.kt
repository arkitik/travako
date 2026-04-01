package io.arkitik.travako.protocol.job

import io.arkitik.travako.protocol.job.dto.TravakoJobExecutionData
import io.arkitik.travako.protocol.job.dto.TravakoJobExecutionResult

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:53 PM, 02/09/2025
 */
interface StatefulTravakoJob {
    fun executeJob(executionData: TravakoJobExecutionData): TravakoJobExecutionResult
}