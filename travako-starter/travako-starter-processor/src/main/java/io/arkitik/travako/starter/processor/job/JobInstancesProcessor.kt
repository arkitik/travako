package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.starter.job.source.JobInstancesSource
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.job.nextTimeToExecution
import io.arkitik.travako.starter.processor.core.job.parseTrigger
import io.arkitik.travako.starter.processor.core.logger.logger

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 9:57 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class JobInstancesProcessor(
    private val travakoConfig: TravakoConfig,
    private val jobInstancesSource: JobInstancesSource,
    private val jobInstanceSdk: JobInstanceSdk,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
) : Processor<JobInstanceDomain> {
    companion object {
        private val logger = logger<JobInstancesProcessor>()
    }

    override val type = JobInstanceDomain::class.java

    override fun process() {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstancesSource.jobs()
                .forEach { jobInstanceBean ->
                    try {
                        val jobTrigger = jobInstanceBean.trigger.parseTrigger()
                        val nextExecution = jobInstanceBean.trigger.nextTimeToExecution()
                        jobInstanceSdk.registerJob
                            .runOperation(
                                CreateJobDto(
                                    serverKey = travakoConfig.serverKey,
                                    jobKey = jobInstanceBean.jobKey,
                                    jobTrigger = jobTrigger.first,
                                    isDuration = jobTrigger.second,
                                    nextExecution = nextExecution,
                                    jobClassName = jobInstanceBean.javaClass.name,
                                    params = mapOf()
                                )
                            )
                    } catch (e: Exception) {
                        logger.warn(
                            "Error while registering the Job Instance: [Key: {}] [Error: {}]",
                            jobInstanceBean.jobKey,
                            e.message
                        )
                    }
                }
        }
    }
}
