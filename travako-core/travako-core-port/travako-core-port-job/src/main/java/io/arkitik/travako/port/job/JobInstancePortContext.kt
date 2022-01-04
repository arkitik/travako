package io.arkitik.travako.port.job

import io.arkitik.travako.adapter.job.JobInstanceStoreImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.operation.job.JobDomainSdkImpl
import io.arkitik.travako.operation.job.JobInstanceSdkImpl
import io.arkitik.travako.sdk.domain.job.JobDomainSdk
import io.arkitik.travako.sdk.domain.runner.SchedulerRunnerDomainSdk
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.sdk.job.event.JobEventSdk
import io.arkitik.travako.store.job.JobInstanceStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class JobInstancePortContext {
    @Bean
    fun jobInstanceStore(
        travakoJobInstanceRepository: TravakoJobInstanceRepository,
    ): JobInstanceStore = JobInstanceStoreImpl(travakoJobInstanceRepository)

    @Bean
    fun jobDomainSdk(
        jobInstanceStore: JobInstanceStore,
    ): JobDomainSdk = JobDomainSdkImpl(jobInstanceStore)

    @Bean
    fun jobInstanceSdk(
        jobInstanceStore: JobInstanceStore,
        serverDomainSdk: ServerDomainSdk,
        schedulerRunnerDomainSdk: SchedulerRunnerDomainSdk,
        jobEventSdk: JobEventSdk,
    ): JobInstanceSdk = JobInstanceSdkImpl(
        jobInstanceStore = jobInstanceStore,
        serverDomainSdk = serverDomainSdk,
        schedulerRunnerDomainSdk = schedulerRunnerDomainSdk,
        jobEventSdk = jobEventSdk
    )
}
