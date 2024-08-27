package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.sdk.job.dto.UpdateJobRequest
import io.arkitik.travako.starter.job.bean.TravakoJob
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.job.nextTimeToExecution
import io.arkitik.travako.starter.processor.core.logger.logger
import org.springframework.scheduling.Trigger
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 7:12 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobExecutor(
    private val travakoConfig: TravakoConfig,
    private val travakoRunnerConfig: TravakoRunnerConfig,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val jobInstanceSdk: JobInstanceSdk,
) {
    companion object {
        private val logger = logger<RunnerJobExecutor>()
    }

    fun executeJob(jobDetails: JobDetails, trigger: Trigger, travakoJob: TravakoJob) {
        jobInstanceSdk.isJobAssignedToRunner
            .operateRole(
                request = JobServerRunnerKeyDto(
                    serverKey = travakoConfig.serverKey,
                    runnerKey = travakoRunnerConfig.key,
                    runnerHost = travakoRunnerConfig.host,
                    jobKey = jobDetails.jobKey
                )
            ).takeIf { it }?.also {
                jobDetails.markAsRunning(null)

                runCatching {
                    travakoJob.execute(
                        TravakoJobExecutionData(
                            serverKey = travakoConfig.serverKey,
                            runnerKey = travakoRunnerConfig.key,
                            runnerHost = travakoRunnerConfig.host,
                            jobKey = jobDetails.jobKey,
                            params = jobDetails.params
                        )
                    )
                }.onFailure {
                    logger.error("Error while executing job [key: ${jobDetails.jobKey}]", it)
                }.also {
                    val nextExecution = trigger.nextTimeToExecution()
                    jobDetails.markAsWaiting(nextExecution)
                }
            }
    }

    private fun JobDetails.markAsRunning(nextExecution: LocalDateTime?) {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstanceSdk.markJobAsRunning.runOperation(
                UpdateJobRequest(
                    jobKey = JobKeyDto(
                        serverKey = travakoConfig.serverKey,
                        jobKey = jobKey
                    ),
                    nextExecutionTime = nextExecution
                )
            )
        }
    }

    private fun JobDetails.markAsWaiting(nextExecution: LocalDateTime?) {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstanceSdk.markJobAsWaiting.runOperation(
                UpdateJobRequest(
                    jobKey = JobKeyDto(
                        serverKey = travakoConfig.serverKey,
                        jobKey = jobKey
                    ),
                    nextExecutionTime = nextExecution
                )
            )
        }
    }
}
