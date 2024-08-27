package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.job.bean.JobInstanceRestartProcessor
import io.arkitik.travako.starter.job.registry.JobInstancesRegistry
import io.arkitik.travako.starter.processor.core.logger.logger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 9:42 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstanceRestartProcessorImpl(
    private val jobInstancesRegistry: JobInstancesRegistry,
) : JobInstanceRestartProcessor {
    companion object {
        private val logger = logger<JobInstanceRestartProcessorImpl>()
    }

    override fun triggerRestartJob(jobInstanceBean: JobInstanceBean) {
        try {
            jobInstancesRegistry.updateJobTrigger(jobInstanceBean.jobKey, jobInstanceBean.trigger)
        } catch (e: UnprocessableEntityException) {
            logger.warn(
                "Error while updating Job Instance: [Key: {}] [Error: {}]",
                jobInstanceBean.jobKey,
                e.error
            )
        }
    }
}
