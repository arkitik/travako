package io.arkitik.travako.starter.processor.runner

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.starter.job.source.JobInstancesSource
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:58 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerJobsRegisterProcessor(
    private val jobInstancesSource: JobInstancesSource,
    private val jobsSchedulerRegistry: JobsSchedulerRegistry,
) : Processor<SchedulerRunnerDomain> {
    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        jobInstancesSource
            .forEach { jobInstanceBean ->
                jobsSchedulerRegistry.scheduleJob(jobInstanceBean)
            }
    }
}
