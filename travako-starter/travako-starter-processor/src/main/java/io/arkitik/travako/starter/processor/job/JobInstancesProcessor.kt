package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.starter.job.source.JobInstancesSource
import io.arkitik.travako.starter.processor.config.TravakoConfig
import org.slf4j.LoggerFactory

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
        private val logger = LoggerFactory.getLogger(JobInstancesProcessor::class.java)!!
    }

    override val type = JobInstanceDomain::class.java

    override fun process() {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstancesSource.forEach { job ->
                try {
                    val jobTrigger = job.trigger.parseTrigger()
                    jobInstanceSdk.registerJob
                        .runOperation(
                            CreateJobDto(
                                serverKey = travakoConfig.serverKey,
                                jobKey = job.jobKey,
                                jobTrigger = jobTrigger.first,
                                isDuration = jobTrigger.second
                            )
                        )
                } catch (e: Exception) {
                    logger.warn(
                        "Error while registering the Job Instance: [Key: {}] [Error: {}]",
                        job.jobKey,
                        e.message
                    )
                }
            }
        }
    }
}
