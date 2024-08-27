package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.AssignJobsToRunnerDto
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobServerDto
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoLeaderConfig
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.errors.StartupErrors
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:47 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class LeaderJobsAssigneeProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val jobInstanceSdk: JobInstanceSdk,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val leaderSdk: LeaderSdk,
    private val travakoLeaderConfig: TravakoLeaderConfig,
    private val travakoRunnerConfig: TravakoRunnerConfig,
) : Processor<LeaderDomain> {
    companion object {
        private val logger = logger<LeaderJobsAssigneeProcessor>()
    }

    override val type = LeaderDomain::class.java

    override fun process() {
        travakoLeaderConfig.jobsAssignee.fixedRateJob(taskScheduler) {
            leaderSdk.isLeaderBefore
                .operateRole(
                    IsLeaderBeforeDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoRunnerConfig.key,
                        runnerHost = travakoRunnerConfig.host,
                        dateBefore = LocalDateTime.now()
                    )
                ).takeIf { it }?.let {
                    travakoTransactionalExecutor.runUnitTransaction {
                        logger.info("Start jobs reassign process...")
                        val runners = schedulerRunnerSdk.allRunningServerRunners
                            .runOperation(RunnerServerKeyDto(travakoConfig.serverKey))
                        if (runners.isEmpty()) {
                            throw StartupErrors.NO_REGISTERED_RUNNERS.internal()
                        }
                        val jobs = jobInstanceSdk.serverJobs
                            .runOperation(JobServerDto(travakoConfig.serverKey))
                        if (jobs.isEmpty()) {
                            throw StartupErrors.NO_REGISTERED_JOBS.internal()
                        }
                        jobs.filterNot(JobDetails::isRunning)
                            .map { job ->
                                runners[Random().nextInt(runners.size)] to job
                            }.groupBy {
                                it.first.runnerKey to it.first.runnerHost
                            }
                            .forEach { (key, value) ->
                                val jobKeys = value
                                    .map { it.second.jobKey }
                                jobInstanceSdk.assignJobsToRunner
                                    .runOperation(
                                        AssignJobsToRunnerDto(
                                            serverKey = travakoConfig.serverKey,
                                            runnerKey = key.first,
                                            runnerHost = key.second,
                                            jobKeys = jobKeys
                                        )
                                    )
                                logger.info(
                                    "Jobs have been reassigned, [Runner: {}] [Jobs: {}]",
                                    "${key.first}-${key.second}",
                                    jobKeys
                                )
                            }
                    }
                }
        }
    }

}
