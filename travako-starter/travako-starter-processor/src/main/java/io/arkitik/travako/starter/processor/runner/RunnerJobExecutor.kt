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
import io.arkitik.travako.starter.job.bean.StatefulTravakoJob
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionData
import io.arkitik.travako.starter.job.bean.dto.TravakoJobExecutionResult
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

    fun executeJob(jobDetails: JobDetails, trigger: Trigger, travakoJob: StatefulTravakoJob) {
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

                logger.trace("Start job execution. [key: {}]", jobDetails.jobKey)
                runCatching {
                    travakoJob.executeJob(
                        TravakoJobExecutionData(
                            serverKey = travakoConfig.serverKey,
                            runnerKey = travakoRunnerConfig.key,
                            runnerHost = travakoRunnerConfig.host,
                            jobKey = jobDetails.jobKey,
                            params = jobDetails.params
                        )
                    )
                }.onFailure {
                    logger.error("Error while executing job [key: {}]", jobDetails.jobKey, it)
                }.onSuccess { jobExecutionResult ->
                    logger.trace("Job executed successfully. [key: {}]", jobDetails.jobKey)
                    if (jobDetails.singleRun && jobExecutionResult is TravakoJobExecutionResult.Companion.Success) {
                        jobDetails.markAsDone()
                    }
                }.also { result ->
                    if (!jobDetails.singleRun || !result.isSuccess || result.getOrNull() is TravakoJobExecutionResult.Companion.Failure) {
                        val nextExecution = trigger.nextTimeToExecution()
                        jobDetails.markAsWaiting(nextExecution)
                    }
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

    private fun JobDetails.markAsDone() {
        travakoTransactionalExecutor.runUnitTransaction {
            jobInstanceSdk.markJobAsDone.runOperation(
                JobKeyDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobKey
                )
            )
        }
    }
}
