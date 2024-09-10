package io.arkitik.travako.starter.job.bean

import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import org.springframework.scheduling.Trigger

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 10:15 AM, 27/08/2024
 */
interface JobInstanceBean : TravakoJob {
    override fun execute(executionData: TravakoJobExecutionData) {
        runJob()
    }

    val jobKey: String
        get() = javaClass.simpleName

    val trigger: Trigger

    val singleRun: Boolean
        get() = false

    fun runJob()
}
