package io.arkitik.travako.starter.startup.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.startup.processor.config.TravakoConfig
import org.slf4j.LoggerFactory

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 9:57 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstancesProcessor(
    private val travakoConfig: TravakoConfig,
    private val jobInstances: List<JobInstanceBean>,
    private val jobInstanceSdk: JobInstanceSdk,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<JobInstanceDomain> {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JobInstancesProcessor::class.java)!!
    }

    override val type = JobInstanceDomain::class.java

    override fun process() {
        transactionalExecutor.runOnTransaction {
            jobInstances.forEach { job ->
                try {
                    jobInstanceSdk.registerJob
                        .runOperation(JobKeyDto(
                            serverKey = travakoConfig.serverKey,
                            jobKey = job.jobKey
                        ))
                } catch (e: UnprocessableEntityException) {
                    LOGGER.error(
                        "Error while registering the Job Instance: [Key: {}] [Error: {}]",
                        job.jobKey,
                        e.error
                    )
                }
            }
        }
    }
}
