package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobServerDto
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:58 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerJobsRegisterProcessor(
    private val jobsSchedulerRegistry: JobsSchedulerRegistry,
    private val travakoConfig: TravakoConfig,
    private val jobInstanceSdk: JobInstanceSdk,
    private val travakoJobInstanceProvider: TravakoJobInstanceProvider,
) : Processor<SchedulerRunnerDomain> {
    companion object {
        private val logger = LoggerFactory.getLogger(RunnerJobsRegisterProcessor::class.java)
    }

    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        jobInstanceSdk.serverJobs
            .runOperation(JobServerDto(travakoConfig.serverKey))
            .forEach { jobDetails ->
                travakoJobInstanceProvider.runCatching {
                    jobsSchedulerRegistry.scheduleJob(
                        jobDetails = jobDetails,
                        travakoJob = provideJobInstance(
                            jobKey = jobDetails.jobKey,
                            jobClassName = jobDetails.jobClassName
                        )
                    )
                }.onFailure { throwable ->
                    logger.error(
                        "Error while running job {}, no provider configured for Job-Class: {}",
                        jobDetails.jobKey,
                        jobDetails.jobClassName,
                        throwable
                    )
                }
            }
    }
}
