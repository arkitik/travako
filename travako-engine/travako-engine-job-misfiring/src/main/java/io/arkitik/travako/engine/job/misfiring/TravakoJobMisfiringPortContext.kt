package io.arkitik.travako.engine.job.misfiring

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.engine.job.misfiring.config.TravakoJobMisfiringConfig
import io.arkitik.travako.engine.job.misfiring.processor.TravakoJobMisfiringProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.config.TravakoRunnerConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.runner.RunnerJobExecutor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:06 PM, 25/08/2024
 */
@Configuration
@EnableConfigurationProperties(TravakoJobMisfiringConfig::class)
class TravakoJobMisfiringPortContext {

    @Bean
    @ConditionalOnProperty(
        prefix = "arkitik.travako.config.misfiring",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = false
    )
    fun travakoJobMisfiringProcessor(
        jobInstanceSdk: JobInstanceSdk,
        travakoJobMisfiringConfig: TravakoJobMisfiringConfig,
        runnerJobExecutor: RunnerJobExecutor,
        taskScheduler: TaskScheduler,
        travakoConfig: TravakoConfig,
        travakoJobInstanceProvider: TravakoJobInstanceProvider,
        travakoRunnerConfig: TravakoRunnerConfig,
    ): Processor<LeaderDomain> =
        TravakoJobMisfiringProcessor(
            jobInstanceSdk = jobInstanceSdk,
            travakoJobMisfiringConfig = travakoJobMisfiringConfig,
            runnerJobExecutor = runnerJobExecutor,
            travakoJobInstanceProvider = travakoJobInstanceProvider,
            taskScheduler = taskScheduler,
            travakoConfig = travakoConfig,
            travakoRunnerConfig = travakoRunnerConfig,
        )
}
