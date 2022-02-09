package io.arkitik.travako.starter.processor.runner

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.processor.config.TravakoConfig

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 7:12 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobExecutor(
    private val travakoConfig: TravakoConfig,
    private val transactionalExecutor: TransactionalExecutor,
    private val jobInstanceSdk: JobInstanceSdk,
) {
    fun executeJob(jobInstanceBean: JobInstanceBean) {
        jobInstanceSdk.isJobAssignedToRunner.operateRole(
            request = JobServerRunnerKeyDto(
                serverKey = travakoConfig.keyDto.serverKey,
                runnerKey = travakoConfig.keyDto.runnerKey,
                runnerHost = travakoConfig.keyDto.runnerHost,
                jobKey = jobInstanceBean.jobKey
            )
        ).takeIf { it }?.also {
            transactionalExecutor.runUnitTransaction {
                jobInstanceSdk.markJobAsRunning.runOperation(
                    JobKeyDto(
                        serverKey = travakoConfig.serverKey,
                        jobKey = jobInstanceBean.jobKey
                    ))
            }
            transactionalExecutor.runUnitTransaction {
                jobInstanceBean.runJob()
            }
            transactionalExecutor.runUnitTransaction {
                jobInstanceSdk.markJobAsWaiting.runOperation(JobKeyDto(
                    serverKey = travakoConfig.serverKey,
                    jobKey = jobInstanceBean.jobKey
                ))
            }
        }
    }
}
