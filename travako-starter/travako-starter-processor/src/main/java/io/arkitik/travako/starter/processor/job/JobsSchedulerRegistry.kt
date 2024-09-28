package io.arkitik.travako.starter.processor.job

import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.starter.job.bean.TravakoJob
import io.arkitik.travako.starter.processor.core.job.asTrigger
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.runner.RunnerJobExecutor
import io.arkitik.travako.starter.processor.scheduler.buildJob
import org.springframework.scheduling.TaskScheduler
import java.util.concurrent.ScheduledFuture

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 6:47 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobsSchedulerRegistry(
    private val taskScheduler: TaskScheduler,
    private val runnerJobExecutor: RunnerJobExecutor,
) {
    companion object {
        private val logger = logger<JobsSchedulerRegistry>()
    }

    private val jobTriggers = hashMapOf<String, ScheduledFuture<*>?>()

    fun scheduleJob(jobDetails: JobDetails, travakoJob: TravakoJob) {
        logger.debug("Scheduling JOB instance {}", jobDetails.jobKey)
        val trigger = jobDetails.asTrigger()
        if (jobTriggers.containsKey(jobDetails.jobKey)) {
            logger.warn("Job already scheduled, restart job trigger {}", jobDetails.jobKey)
            deleteJob(jobDetails.jobKey)
        }
        val scheduledFuture = trigger.buildJob(taskScheduler) {
            runnerJobExecutor.executeJob(jobDetails, trigger, travakoJob)
        }
        jobTriggers[jobDetails.jobKey] = scheduledFuture
    }

    fun deleteJob(jobKey: String) {
        logger.debug("Deleting JOB instance {}", jobKey)
        jobTriggers[jobKey]?.cancel(false)
    }

    fun rebootScheduledJob(jobDetails: JobDetails, travakoJob: TravakoJob) {
        jobTriggers[jobDetails.jobKey]?.cancel(false)
        val trigger = jobDetails.asTrigger()
        logger.debug("Restart JOB instance {}", jobDetails.jobKey)
        try {
            val newScheduledFuture = trigger.buildJob(taskScheduler) {
                runnerJobExecutor.executeJob(jobDetails, trigger, travakoJob)
            }
            jobTriggers[jobDetails.jobKey] = newScheduledFuture
        } catch (e: Exception) {
            logger.warn(
                "Error while restart the Job Instance: [Key: {}] [Error: {}]",
                jobDetails.jobKey,
                e.message
            )
        }
    }
}
