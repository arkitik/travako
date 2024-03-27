package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
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
) : Processor<SchedulerRunnerDomain> {
    override val type = SchedulerRunnerDomain::class.java
    private val logger = logger<RunnerJobRestartProcessor>()
    private val eventProcessors = hashMapOf<String, (EventDataDto) -> Unit>()

    init {
        eventProcessors[JobEventType.RESTART.name] = { event ->
            jobInstances.firstOrNull { job ->
                event.jobKey == job.jobKey
            }?.let { job ->
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
                            serverKey = travakoConfig.keyDto.serverKey,
                            runnerKey = travakoConfig.keyDto.runnerKey,
                            runnerHost = travakoConfig.keyDto.runnerHost
                        )
                    ).events
                        .forEach { event ->
                            transactionalExecutor.runUnitTransaction {
                                eventProcessors[event.eventType]?.let { function ->
                                    logger.debug("Start {} processor for [Job-Key {}]", event.eventType, event.jobKey)
                                    function(event)
                                    jobEventSdk.markEventProcessedForRunner.runOperation(
                                        JobEventRunnerKeyAndUuidDto(
                                            serverKey = travakoConfig.keyDto.serverKey,
                                            runnerKey = travakoConfig.keyDto.runnerKey,
                                            runnerHost = travakoConfig.keyDto.runnerHost,
                                            eventUuid = event.eventUuid
                                        )
                                    )
                                }
                            }
                        }
                }.onFailure {
                    logger.warn(
                        "Error while fetching pending events for runner {}, cause {}",
                        "${travakoConfig.keyDto.runnerKey}-${travakoConfig.keyDto.runnerHost}",
                        it.message
                    )
                }
            }
    }
}
