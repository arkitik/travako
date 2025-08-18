package io.arkitik.travako.port.job.event

import io.arkitik.travako.operation.job.event.JobEventSdkImpl
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class JobEventPortContext {
    @Bean
    fun jobEventSdk(
        jobEventStore: JobEventStore,
        runnerJobEventStateStore: RunnerJobEventStateStore,
        jobDomainSdk: JobDomainSdk,
        schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
        serverDomainSdk: ServerDomainSdk,
    ): JobEventSdk = JobEventSdkImpl(
        jobEventStore = jobEventStore,
        runnerJobEventStateStore = runnerJobEventStateStore,
        jobDomainSdk = jobDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        serverDomainSdk = serverDomainSdk,
    )
}
