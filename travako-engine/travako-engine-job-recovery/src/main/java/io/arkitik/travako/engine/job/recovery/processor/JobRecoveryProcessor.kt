package io.arkitik.travako.engine.job.recovery.processor

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.JobRunnerKeyDto
import io.arkitik.travako.sdk.job.dto.JobServerDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import io.arkitik.travako.sdk.runner.dto.RunnerServerKeyDto
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.slf4j.LoggerFactory
import org.springframework.scheduling.TaskScheduler
import java.time.Duration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:06 PM, 26 , **Sat, Aug 2023**
 */
internal class JobRecoveryProcessor(
    private val jobInstanceSdk: JobInstanceSdk,
    private val schedulerRunnerSdk: SchedulerRunnerSdk,
    private val taskScheduler: TaskScheduler,
    private val travakoConfig: TravakoConfig,
    private val transactionalExecutor: TransactionalExecutor,
) : Processor<LeaderDomain> {
    companion object {
        private val logger = LoggerFactory.getLogger(JobRecoveryProcessor::class.java)
    }

    override val type: Class<LeaderDomain> = LeaderDomain::class.java

    override fun process() {
        Duration.ofSeconds(travakoConfig.jobsAssignee.seconds.times(2))
            .fixedRateJob(taskScheduler) {
                logger.debug("Start jobs-recovery-processor")
                transactionalExecutor.runUnitTransaction {
                    val runningJobs = jobInstanceSdk.serverJobs
                        .runOperation(
                            JobServerDto(
                                serverKey = travakoConfig.serverKey
                            )
                        ).filter(JobDetails::isRunning)
                    schedulerRunnerSdk.allServerRunners
                        .runOperation(
                            RunnerServerKeyDto(
                                serverKey = travakoConfig.serverKey
                            )
                        ).filterNot(RunnerDetails::isRunning)
                        .associateWith { runner ->
                            runningJobs.filter { job ->
                                jobInstanceSdk.isJobAssignedToRunner
                                    .operateRole(
                                        JobServerRunnerKeyDto(
                                            serverKey = travakoConfig.serverKey,
                                            runnerKey = runner.runnerKey,
                                            runnerHost = runner.runnerHost,
                                            jobKey = job.jobKey
                                        )
                                    )
                            }
                        }.filterValues(List<JobDetails>::isNotEmpty)
                        .forEach { (runner, jobs) ->
                            logger.debug(
                                "Jobs-recovery-processor detect inactive runner and jobs assigned to it, start remove-jobs assignee for runner: [key: {}, host: {}]",
                                runner.runnerKey,
                                runner.runnerHost
                            )
                            jobInstanceSdk.removeRunnerJobsAssignee
                                .runOperation(
                                    JobRunnerKeyDto(
                                        serverKey = travakoConfig.serverKey,
                                        runnerKey = runner.runnerKey,
                                        runnerHost = runner.runnerHost,
                                    )
                                )
                            jobs.forEach { job ->
                                logger.debug(
                                    "Jobs-recovery-processor detect inactive runner and jobs assigned to it, marking job as WAITING for runner: [key: {}, host: {}], job: [key: {}]",
                                    runner.runnerKey,
                                    runner.runnerHost,
                                    job.jobKey
                                )
                                jobInstanceSdk.markJobAsWaiting
                                    .runOperation(
                                        JobKeyDto(
                                            serverKey = travakoConfig.serverKey,
                                            jobKey = job.jobKey
                                        )
                                    )
                            }

                        }
                }
            }
    }
}