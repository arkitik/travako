package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.function.transaction.runUnitTransaction
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.sdk.job.dto.UpdateJobRequest
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.config.TravakoConfig
import io.arkitik.travako.starter.processor.logger.logger
import org.springframework.scheduling.support.SimpleTriggerContext
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 7:12 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobExecutor(
    private val travakoConfig: TravakoConfig,
    private val travakoTransactionalExecutor: TravakoTransactionalExecutor,
    private val jobInstanceSdk: JobInstanceSdk,
) {
    companion object {
        private val logger = logger<RunnerJobExecutor>()
    }

    fun executeJob(jobInstanceBean: JobInstanceBean) {
        jobInstanceSdk.isJobAssignedToRunner.operateRole(
            request = JobServerRunnerKeyDto(
                serverKey = travakoConfig.keyDto.serverKey,
                runnerKey = travakoConfig.keyDto.runnerKey,
                runnerHost = travakoConfig.keyDto.runnerHost,
                jobKey = jobInstanceBean.jobKey
            )
        ).takeIf { it }?.also {
            val instant = jobInstanceBean.trigger.nextExecution(SimpleTriggerContext())
            val nextExecution = instant?.atZone(ZoneId.systemDefault())?.toLocalDateTime()

            jobInstanceBean.markAsRunning(nextExecution)

            runCatching {
                jobInstanceBean.runJob()
            }.onFailure {
                logger.error("Error while executing job [key: ${jobInstanceBean.jobKey}]", it)
            }.also {
                jobInstanceBean.markAsWaiting(nextExecution)
            }
        }
    }

    private fun JobInstanceBean.markAsRunning(nextExecution: LocalDateTime?) {
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

    private fun JobInstanceBean.markAsWaiting(nextExecution: LocalDateTime?) {
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
