package io.arkitik.travako.engine.job.misfiring.processor

import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.engine.job.misfiring.config.TravakoJobMisfiringConfig
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.dto.JobDetails
import io.arkitik.travako.sdk.job.dto.JobServerRunnerKeyNextExecutionDto
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.job.asTrigger
import io.arkitik.travako.starter.processor.core.logger.logger
import io.arkitik.travako.starter.processor.runner.RunnerJobExecutor
import io.arkitik.travako.starter.processor.scheduler.fixedRateJob
import org.springframework.scheduling.TaskScheduler
import java.time.Duration
import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:12 PM, 25/08/2024
 */
internal class TravakoJobMisfiringProcessor(
    private val jobInstanceSdk: JobInstanceSdk,
    private val travakoJobMisfiringConfig: TravakoJobMisfiringConfig,
    private val travakoConfig: TravakoConfig,
    private val runnerJobExecutor: RunnerJobExecutor,
    private val taskScheduler: TaskScheduler,
    private val travakoJobInstanceProvider: TravakoJobInstanceProvider,
    private val travakoRunnerConfig: TravakoRunnerConfig,
) : Processor<LeaderDomain> {
    companion object {
        private val logger = logger<TravakoJobMisfiringProcessor>()
    }

    override val type: Class<LeaderDomain> = LeaderDomain::class.java

    override fun process() {
        Duration.ofMinutes(travakoJobMisfiringConfig.runIntervalMinutes).fixedRateJob(taskScheduler) {
            jobInstanceSdk.runnerJobsWithDueNextExecutionTime
                .runOperation(
                    JobServerRunnerKeyNextExecutionDto(
                        serverKey = travakoConfig.serverKey,
                        runnerKey = travakoRunnerConfig.key,
                        runnerHost = travakoRunnerConfig.host,
                        executionTime = LocalDateTime.now().minusSeconds(travakoJobMisfiringConfig.thresholdSeconds)
                    )
                )
                .filterNot(JobDetails::isRunning)
                .forEach { job ->
                    logger.warn(
                        "A misfire has been detected for job {}. The job will start running now to recover.",
                        job.jobKey,
                    )
                    val travakoJob = travakoJobInstanceProvider.provideJobInstance(job.jobKey, job.jobClassName)
                    runnerJobExecutor.executeJob(job, job.asTrigger(), travakoJob)
                }
        }
    }
}
