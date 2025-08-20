package io.arkitik.travako.adapter.leader

import io.arkitik.travako.operation.leader.LeaderDomainSdkImpl
import io.arkitik.travako.operation.leader.LeaderSdkImpl
import io.arkitik.travako.sdk.domain.leader.LeaderDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.leader.LeaderSdk
import io.arkitik.travako.store.leader.LeaderStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:50 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class LeaderPortContext {
    @Bean
    fun leaderDomainSdk(
        leaderStore: LeaderStore,
    ): LeaderDomainSdk = LeaderDomainSdkImpl(leaderStore = leaderStore)

    @Bean
    fun leaderSdk(
        leaderStore: LeaderStore,
        serverDomainSdk: ServerDomainSdk,
        schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
        leaderDomainSdk: LeaderDomainSdk,
    ): LeaderSdk = LeaderSdkImpl(
        leaderStore = leaderStore,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        leaderDomainSdk = leaderDomainSdk,
    )
}
