package io.arkitik.travako.starter.processor.job

import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.logger.logger
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
    private val jobTriggers = hashMapOf<String, ScheduledFuture<*>?>()
    private val logger = logger<JobsSchedulerRegistry>()

    fun scheduleJob(jobInstance: JobInstanceBean) {
        val scheduledFuture = jobInstance.trigger.buildJob(taskScheduler) {
            runnerJobExecutor.executeJob(jobInstance)
        }
        jobTriggers[jobInstance.jobKey] = scheduledFuture
    }

    fun rebootScheduledJob(jobInstance: JobInstanceBean) {
        jobTriggers[jobInstance.jobKey]?.cancel(false)
        logger.debug("Restart JOB instance {}", jobInstance.jobKey)
        try {
            val newScheduledFuture = jobInstance.trigger.buildJob(taskScheduler) {
                runnerJobExecutor.executeJob(jobInstance)
            }
            jobTriggers[jobInstance.jobKey] = newScheduledFuture
        } catch (e: Exception) {
            logger.warn(
                "Error while restart the Job Instance: [Key: {}] [Error: {}]",
                jobInstance.jobKey,
                e.message
            )
        }
    }
}
