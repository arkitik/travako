package io.arkitik.travako.starter.processor.job

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.error.Error
import io.arkitik.radix.develop.shared.exception.InternalException
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.config.TravakoConfig
import org.slf4j.LoggerFactory
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
import java.util.concurrent.TimeUnit

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

    private val timeUnitsMapper = hashMapOf<TimeUnit, String>()

    init {
        timeUnitsMapper[TimeUnit.DAYS] = "d"
        timeUnitsMapper[TimeUnit.HOURS] = "h"
        timeUnitsMapper[TimeUnit.MINUTES] = "m"
        timeUnitsMapper[TimeUnit.SECONDS] = "s"
        timeUnitsMapper[TimeUnit.MILLISECONDS] = "ms"
    }

    override fun process() {
        transactionalExecutor.runUnitTransaction {
            jobInstances.forEach { job ->
                val jobTrigger =
                    when (job.trigger) {
                        is CronTrigger -> {
                            (job.trigger as CronTrigger).expression to false
                        }
                        is PeriodicTrigger -> {
                            val trigger = job.trigger as PeriodicTrigger
                            "${trigger.period}${timeUnitsMapper[trigger.timeUnit]}" to true
                        }
                        else -> {
                            throw InternalException(Error("INTERNAL-ERROR",
                                "trigger is not supported ${job.trigger}"))
                        }
                    }
                try {
                    jobInstanceSdk.registerJob
                        .runOperation(
                            CreateJobDto(
                                serverKey = travakoConfig.serverKey,
                                jobKey = job.jobKey,
                                jobTrigger = jobTrigger.first,
                                isDuration = jobTrigger.second
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
