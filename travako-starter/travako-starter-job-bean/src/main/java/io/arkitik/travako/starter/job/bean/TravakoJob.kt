package io.arkitik.travako.starter.job.bean

import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:52 PM, 26/08/2024
 */
interface TravakoJob {
    fun execute(executionData: TravakoJobExecutionData)
}
