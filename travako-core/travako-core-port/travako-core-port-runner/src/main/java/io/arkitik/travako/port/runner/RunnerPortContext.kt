package io.arkitik.travako.port.runner

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.operation.runner.SchedulerRunnerDomainSdkImpl
import io.arkitik.travako.operation.runner.SchedulerRunnerSdkImpl
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.runner.SchedulerRunnerSdk
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class RunnerPortContext {
    @Bean
    fun schedulerRunnerSdk(
        schedulerRunnerStore: SchedulerRunnerStore,
        serverDomainSdk: ServerDomainSdk,
        leaderDomainSdk: LeaderDomainSdk,
        schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
    ): SchedulerRunnerSdk = SchedulerRunnerSdkImpl(
        schedulerRunnerStore = schedulerRunnerStore,
        serverDomainSdk = serverDomainSdk,
        leaderDomainSdk = leaderDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk
    )

    @Bean
    fun schedulerRunnerDomainSdk(
        schedulerRunnerStore: SchedulerRunnerStore,
        travakoTransactionalExecutor: TravakoTransactionalExecutor,
    ): SchedulerRunnerDomainSdk = SchedulerRunnerDomainSdkImpl(
        schedulerRunnerStore = schedulerRunnerStore,
        travakoTransactionalExecutor = travakoTransactionalExecutor,
    )
}
