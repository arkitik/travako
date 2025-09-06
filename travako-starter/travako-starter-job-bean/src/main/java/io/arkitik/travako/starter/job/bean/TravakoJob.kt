package io.arkitik.travako.starter.job.bean

import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionResult

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:52 PM, 26/08/2024
 */
interface TravakoJob : StatefulTravakoJob {
    override fun executeJob(executionData: TravakoJobExecutionData): TravakoJobExecutionResult {
        execute(executionData)
        return TravakoJobExecutionResult.success()
    }

    fun execute(executionData: TravakoJobExecutionData)
}
