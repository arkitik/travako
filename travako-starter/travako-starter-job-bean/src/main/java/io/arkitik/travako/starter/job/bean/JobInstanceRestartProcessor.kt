package io.arkitik.travako.starter.job.bean

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 9:41 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Deprecated(
    message = "This class will be removed in a future version, Instead use JobInstancesRegistry.updateJobTrigger to restart job",
    replaceWith = ReplaceWith(
        expression = "io.arkitik.travako.starter.job.registry.JobInstancesRegistry"
    )
)
interface JobInstanceRestartProcessor {
    fun triggerRestartJob(jobInstanceBean: JobInstanceBean)
}
