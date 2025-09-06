package io.arkitik.travako.starter.job.bean

import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionResult

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:53 PM, 02/09/2025
 */
interface StatefulTravakoJob {
    fun executeJob(executionData: TravakoJobExecutionData): TravakoJobExecutionResult
}