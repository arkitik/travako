package io.arkitik.travako.engine.cleanup

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.engine.cleanup.config.TravakoCleanupConfig
import io.arkitik.travako.engine.cleanup.processors.TravakoCleanupProcessor
import io.arkitik.travako.function.processor.Processor
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.job.event.JobEventDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

/**
 * @author Ibrahim Al-Tamimi 
 * @since 11:19, Tuesday, 31/03/2026
 **/
@Configuration
@EnableConfigurationProperties(TravakoCleanupConfig::class)
@ConditionalOnBooleanProperty(
    prefix = "arkitik.travako.config.cleanup",
    name = ["enabled"],
    havingValue = true,
    matchIfMissing = true,
)
class TravakoCleanupPortContext {
    @Bean
    fun travakoCleanupProcessor(
        serverDomainSdk: ServerDomainSdk,
        jobDomainSdk: JobDomainSdk,
        jobDomainEventSdk: JobEventDomainSdk,
        schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
        taskScheduler: TaskScheduler,
        travakoConfig: TravakoConfig,
        travakoCleanupConfig: TravakoCleanupConfig,
    ): Processor<JobInstanceDomain> =
        TravakoCleanupProcessor(
            serverDomainSdk = serverDomainSdk,
            jobDomainSdk = jobDomainSdk,
            jobDomainEventSdk = jobDomainEventSdk,
            schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
            taskScheduler = taskScheduler,
            travakoConfig = travakoConfig,
            travakoCleanupConfig = travakoCleanupConfig,
        )
}