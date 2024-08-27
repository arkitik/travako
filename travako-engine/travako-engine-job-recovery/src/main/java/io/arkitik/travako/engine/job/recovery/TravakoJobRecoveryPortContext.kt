package io.arkitik.travako.engine.job.recovery

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.engine.job.recovery.processor.JobRecoveryProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.starter.processor.config.TravakoLeaderConfig
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:52 PM, 26 , **Sat, Aug 2023**
 */
@Configuration
class TravakoJobRecoveryPortContext {
    @Bean
    fun jobRecoveryProcessor(
        jobInstanceSdk: JobInstanceSdk,
        schedulerRunnerSdk: SchedulerRunnerSdk,
        taskScheduler: TaskScheduler,
        travakoConfig: TravakoConfig,
        travakoLeaderConfig: TravakoLeaderConfig,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): Processor<LeaderDomain> =
        JobRecoveryProcessor(
            jobInstanceSdk = jobInstanceSdk,
            schedulerRunnerSdk = schedulerRunnerSdk,
            taskScheduler = taskScheduler,
            travakoConfig = travakoConfig,
            travakoTransactionalExecutor = travakoTransactionalExecutor,
            travakoLeaderConfig = travakoLeaderConfig
        )
}
