package io.arkitik.travako.starter.job.bean

import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import org.springframework.scheduling.Trigger

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 10:15 AM, 27/08/2024
 */
@Deprecated(
    message = "will be removed in a future version, instead use TravakoJob for scheduling jobs",
    replaceWith = ReplaceWith("io.arkitik.travako.starter.job.bean.TravakoJob"),
    level = DeprecationLevel.WARNING
)
interface JobInstanceBean : TravakoJob {
    override fun execute(executionData: TravakoJobExecutionData) {
        runJob()
    }

    val jobKey: String
        get() = javaClass.simpleName

    val trigger: Trigger

    fun runJob()
}
