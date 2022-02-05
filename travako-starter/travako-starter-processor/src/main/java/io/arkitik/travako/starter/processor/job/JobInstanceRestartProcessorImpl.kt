package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.JobInstanceRestartProcessor
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.logger.logger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 9:42 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceRestartProcessorImpl(
    private val travakoConfig: TravakoConfig,
    private val jobEventSdk: JobEventSdk,
    private val jobInstanceSdk: JobInstanceSdk,
) : JobInstanceRestartProcessor {
    private val logger = logger<JobInstanceRestartProcessorImpl>()

    override fun triggerRestartJob(jobInstanceBean: JobInstanceBean) {
        try {
            val jobTrigger = jobInstanceBean.trigger.parseTrigger()
            jobInstanceSdk.updateJobTrigger
                .runOperation(
                    CreateJobDto(
                        serverKey = travakoConfig.serverKey,
                        jobKey = jobInstanceBean.jobKey,
                        jobTrigger = jobTrigger.first,
                        isDuration = jobTrigger.second
                    ))
        } catch (e: UnprocessableEntityException) {
            logger.warn(
                "Error while updating Job Instance: [Key: {}] [Error: {}]",
                jobInstanceBean.jobKey,
                e.error
            )
        }
        jobEventSdk.insertRestartJobEvent
            .runOperation(JobEventKeyDto(travakoConfig.serverKey, jobInstanceBean.jobKey))
    }
}
