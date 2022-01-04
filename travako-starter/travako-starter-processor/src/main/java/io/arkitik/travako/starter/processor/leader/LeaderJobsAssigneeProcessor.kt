package io.arkitik.travako.starter.processor.leader

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.shared.ext.internal
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.AssignJobsToRunnerDto
import io.arkitik.travako.sdk.job.dto.JobServerDto
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.sdk.leader.dto.IsLeaderBeforeDto
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.errors.StartupErrors
import io.arkitik.travako.starter.processor.logger.logger
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:47 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderJobsAssigneeProcessor(
    private val travakoConfig: TravakoConfig,
    private val taskScheduler: TaskScheduler,
    private val jobInstanceSdk: JobInstanceSdk,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val transactionalExecutor: TransactionalExecutor,
    private val leaderSdk: LeaderSdk,
) : Processor<LeaderDomain> {
    private val logger = logger<LeaderJobsAssigneeProcessor>()

    override val type = LeaderDomain::class.java

    override fun process() {
        travakoConfig.jobsAssignee
            .fixedRateJob(taskScheduler) {
                leaderSdk.isLeaderBefore
                    .operateRole(IsLeaderBeforeDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoConfig.runnerKey,
                        dateBefore = LocalDateTime.now()
                    )).takeIf { it }?.let {
                        transactionalExecutor.runOnTransaction {
                            logger.info("Start jobs reassign process...")
                            val runners = schedulerRunnerSdk.allServerRunners
                                .runOperation(RunnerServerKeyDto(travakoConfig.serverKey))
                                .filter { it.isRunning }
                            if (runners.isEmpty()) {
                                throw StartupErrors.NO_REGISTERED_RUNNERS.internal()
                            }
                            val jobs = jobInstanceSdk.serverJobs
                                .runOperation(JobServerDto(travakoConfig.serverKey))
                            if (jobs.isEmpty()) {
                                throw StartupErrors.NO_REGISTERED_JOBS.internal()
                            }
                            jobs.filter { it.isRunning.not() }
                                .map { job ->
                                    runners[Random().nextInt(runners.size)] to job
                                }.groupBy { it.first.runnerKey }
                                .forEach { entry ->
                                    val jobKeys = entry.value.map {
                                        it.second.jobKey
                                    }
                                    jobInstanceSdk.assignJobsToRunner.runOperation(
                                        AssignJobsToRunnerDto(
                                            travakoConfig.serverKey,
                                            entry.key,
                                            jobKeys
                                        )
                                    )
                                    logger.info("Jobs have been reassigned, [Runner: {}] [Jobs: {}]",
                                        entry.key,
                                        jobKeys)
                                }
                        }
                    }
            }
    }

}
