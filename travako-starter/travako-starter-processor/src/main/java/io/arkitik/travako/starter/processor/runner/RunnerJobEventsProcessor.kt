package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyAndUuidDto
import io.arkitik.travako.sdk.job.event.dto.JobEventRunnerKeyDto
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.job.JobsSchedulerRegistry
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 7:21 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerJobEventsProcessor(
    private val travakoConfig: TravakoConfig,
    private val travakoRunnerConfig: TravakoRunnerConfig,
    private val taskScheduler: TaskScheduler,
    private val jobEventSdk: JobEventSdk,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val jobsSchedulerRegistry: JobsSchedulerRegistry,
    private val jobInstanceSdk: JobInstanceSdk,
    private val travakoJobInstanceProvider: TravakoJobInstanceProvider,
) : Processor<SchedulerRunnerDomain> {
    companion object {
        private val logger = logger<RunnerJobEventsProcessor>()
    }

    override val type = SchedulerRunnerDomain::class.java
    private val eventProcessors = hashMapOf<String, (JobDetails) -> Unit>()

    init {
        eventProcessors[JobEventType.RESTART.name] = { jobDetails ->
            jobsSchedulerRegistry.rebootScheduledJob(
                jobDetails = jobDetails,
                travakoJob = travakoJobInstanceProvider.provideJobInstance(jobDetails.jobKey, jobDetails.jobClassName)
            )
        }

        eventProcessors[JobEventType.REGISTER.name] = { jobDetails ->
            jobsSchedulerRegistry.scheduleJob(
                jobDetails = jobDetails,
                travakoJob = travakoJobInstanceProvider.provideJobInstance(jobDetails.jobKey, jobDetails.jobClassName)
            )
        }

        eventProcessors[JobEventType.DELETE.name] = { jobDetails ->
            jobsSchedulerRegistry.deleteJob(jobDetails.jobKey)
        }

        eventProcessors[JobEventType.RECOVER.name] = { jobDetails ->
            jobsSchedulerRegistry.rebootScheduledJob(
                jobDetails = jobDetails,
                travakoJob = travakoJobInstanceProvider.provideJobInstance(jobDetails.jobKey, jobDetails.jobClassName)
            )
        }
    }

    override fun process() {
        travakoRunnerConfig.jobsEvent.fixedRateJob(taskScheduler) {
            jobEventSdk.pendingEventsForRunner.runCatching {
                runOperation(
                    JobEventRunnerKeyDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoRunnerConfig.key,
                        runnerHost = travakoRunnerConfig.host,
                    )
                ).events
                    .forEach { event ->
                        travakoTransactionalExecutor.runUnitTransaction {
                            eventProcessors[event.eventType]?.let { function ->
                                logger.debug(
                                    "Start {} processor for [Job-Key {}]",
                                    event.eventType,
                                    event.jobKey
                                )
                                val jobDetails = jobInstanceSdk.jobDetails
                                    .runOperation(
                                        JobKeyDto(
                                            serverKey = travakoConfig.serverKey,
                                            jobKey = event.jobKey
                                        )
                                    )
                                function(jobDetails)
                                jobEventSdk.markEventProcessedForRunner.runOperation(
                                    JobEventRunnerKeyAndUuidDto(
                                        serverKey = travakoConfig.serverKey,
                                        runnerKey = travakoRunnerConfig.key,
                                        runnerHost = travakoRunnerConfig.host,
                                        eventUuid = event.eventUuid
                                    )
                                )
                            }
                        }
                    }
            }.onFailure {
                logger.warn(
                    "Error while fetching pending events for runner {}, cause {}",
                    "${travakoRunnerConfig.key}-${travakoRunnerConfig.host}",
                    it.message
                )
            }
        }
    }

}
