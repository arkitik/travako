package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.error.Error
import io.arkitik.radix.develop.shared.exception.InternalException
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.CreateJobDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.EventDataDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyAndUuidDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.scheduling.support.PeriodicTrigger
import java.util.concurrent.TimeUnit

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 7:21 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobRestartProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val jobEventSdk: JobEventSdk,
    private val transactionalExecutor: TransactionalExecutor,
    private val jobInstances: List<JobInstanceBean>,
    private val jobsSchedulerRegistry: JobsSchedulerRegistry,
    private val jobInstanceSdk: JobInstanceSdk,
) : Processor<SchedulerRunnerDomain> {
    override val type = SchedulerRunnerDomain::class.java
    private val logger = logger<RunnerJobRestartProcessor>()
    private val eventProcessors = hashMapOf<String, (EventDataDto) -> Unit>()
    private val timeUnitsMapper = hashMapOf<TimeUnit, String>()

    init {
        timeUnitsMapper[TimeUnit.DAYS] = "d"
        timeUnitsMapper[TimeUnit.HOURS] = "h"
        timeUnitsMapper[TimeUnit.MINUTES] = "m"
        timeUnitsMapper[TimeUnit.SECONDS] = "s"
        timeUnitsMapper[TimeUnit.MILLISECONDS] = "ms"

        eventProcessors[JobEventType.RESTART.name] = { event ->
            jobInstances.firstOrNull { job ->
                event.jobKey == job.jobKey
            }?.let { job ->
                val jobTrigger =
                    when (job.trigger) {
                        is CronTrigger -> {
                            (job.trigger as CronTrigger).expression to false
                        }
                        is PeriodicTrigger -> {
                            val trigger = job.trigger as PeriodicTrigger
                            "${
                                trigger.timeUnit.convert(trigger.period,
                                    TimeUnit.MILLISECONDS)
                            }${timeUnitsMapper[trigger.timeUnit]}" to true
                        }
                        else -> {
                            throw InternalException(Error("INTERNAL-ERROR",
                                "trigger is not supported ${job.trigger}"))
                        }
                    }
                try {
                    jobInstanceSdk.updateJobTrigger
                        .runOperation(
                            CreateJobDto(
                                serverKey = travakoConfig.serverKey,
                                jobKey = job.jobKey,
                                jobTrigger = jobTrigger.first,
                                isDuration = jobTrigger.second
                            ))
                } catch (e: UnprocessableEntityException) {
                    logger.warn(
                        "Error while updating Job Instance: [Key: {}] [Error: {}]",
                        job.jobKey,
                        e.error
                    )
                }
                jobsSchedulerRegistry.rebootScheduledJob(job)
            }
        }
    }

    override fun process() {
        travakoConfig.jobsEvent
            .fixedRateJob(taskScheduler) {
                jobEventSdk.pendingEventsForRunner.runCatching {
                    runOperation(
                        JobEventRunnerKeyDto(
                            serverKey = travakoConfig.serverKey,
                            runnerKey = travakoConfig.runnerKey
                        )
                    ).events
                        .forEach { event ->
                            transactionalExecutor.runUnitTransaction {
                                eventProcessors[event.eventType]?.let { function ->
                                    logger.debug("Start {} process for [Job-Key {}]", event.eventType, event.jobKey)
                                    function(event)
                                    jobEventSdk.markEventProcessedForRunner.runOperation(
                                        JobEventRunnerKeyAndUuidDto(
                                            serverKey = travakoConfig.serverKey,
                                            runnerKey = travakoConfig.runnerKey,
                                            eventUuid = event.eventUuid
                                        )
                                    )
                                }
                            }
                        }
                }.onFailure {
                    logger.warn("Error while fetching pending events for runner {}, cause {}",
                        travakoConfig.runnerKey,
                        it.message)
                }
            }
    }
}
