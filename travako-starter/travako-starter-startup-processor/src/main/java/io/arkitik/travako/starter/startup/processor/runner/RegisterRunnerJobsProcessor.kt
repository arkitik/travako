package io.arkitik.travako.starter.startup.processor.runner

import io.arkitik.radix.develop.operation.ext.operateRole
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobKeyDto
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyDto
import io.arkitik.travako.starter.startup.processor.config.TravakoConfig
import io.arkitik.travako.starter.job.bean.JobInstanceBean
import io.arkitik.travako.starter.startup.processor.scheduler.buildJob
import org.springframework.scheduling.TaskScheduler

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:58 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterRunnerJobsProcessor(
    private val travakoConfig: TravakoConfig,
    private val transactionalExecutor: TransactionalExecutor,
    private val jobInstances: List<JobInstanceBean>,
    private val taskScheduler: TaskScheduler,
    private val jobInstanceSdk: JobInstanceSdk,
) : Processor<SchedulerRunnerDomain> {
    override val type = SchedulerRunnerDomain::class.java

    override fun process() {
        jobInstances.forEach { jobInstanceBean ->
            jobInstanceBean.trigger.buildJob(taskScheduler) {
                jobInstanceSdk.isJobAssignedToRunner.operateRole(
                    request = JobServerRunnerKeyDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoConfig.runnerKey,
                        jobKey = jobInstanceBean.jobKey
                    )
                ).takeIf { it }?.also {
                    transactionalExecutor.runOnTransaction {
                        jobInstanceSdk.markJobAsRunning.runOperation(
                            JobKeyDto(
                                serverKey = travakoConfig.serverKey,
                                jobKey = jobInstanceBean.jobKey
                            ))
                    }
                    transactionalExecutor.runOnTransaction {
                        jobInstanceBean.runJob()
                    }
                    transactionalExecutor.runOnTransaction {
                        jobInstanceSdk.markJobAsWaiting.runOperation(JobKeyDto(
                            serverKey = travakoConfig.serverKey,
                            jobKey = jobInstanceBean.jobKey
                        ))
                    }
                }
            }
        }
    }
}
