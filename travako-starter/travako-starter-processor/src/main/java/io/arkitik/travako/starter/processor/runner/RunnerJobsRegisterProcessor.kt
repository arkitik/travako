package io.arkitik.travako.starter.processor.runner

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:58 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobsRegisterProcessor(
    private val jobInstances: List<JobInstanceBean>,
    private val jobsSchedulerRegistry: JobsSchedulerRegistry,
) : Processor<SchedulerRunnerDomain> {
    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        jobInstances.forEach { jobInstanceBean ->
            jobsSchedulerRegistry.scheduleJob(jobInstanceBean)
        }
    }
}
