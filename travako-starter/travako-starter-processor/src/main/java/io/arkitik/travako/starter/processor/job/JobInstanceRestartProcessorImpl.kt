package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventKeyDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.JobInstanceRestartProcessor
import io.arkitik.travako.starter.processor.config.TravakoConfig

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 9:42 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceRestartProcessorImpl(
    private val travakoConfig: TravakoConfig,
    private val jobEventSdk: JobEventSdk,
) : JobInstanceRestartProcessor {
    override fun triggerRestartJob(jobInstanceBean: JobInstanceBean) {
        jobEventSdk.insertRestartJobEvent
            .runOperation(JobEventKeyDto(travakoConfig.serverKey, jobInstanceBean.jobKey))
    }
}
