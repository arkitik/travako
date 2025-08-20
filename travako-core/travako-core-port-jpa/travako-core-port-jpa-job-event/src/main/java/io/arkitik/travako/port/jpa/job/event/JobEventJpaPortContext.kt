package io.arkitik.travako.port.jpa.job.event

import io.arkitik.travako.adapter.job.event.JobEventStoreImpl
import io.arkitik.travako.adapter.job.event.RunnerJobEventStateStoreImpl
import io.arkitik.travako.adapter.job.event.repository.TravakoJobEventRepository
import io.arkitik.travako.adapter.job.event.repository.TravakoRunnerJobEventStateRepository
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
class JobEventJpaPortContext {

    @Bean
    fun runnerJobEventStateStore(
        travakoRunnerJobEventStateRepository: TravakoRunnerJobEventStateRepository,
    ): RunnerJobEventStateStore = RunnerJobEventStateStoreImpl(travakoRunnerJobEventStateRepository)

    @Bean
    fun jobEventStore(
        travakoJobEventRepository: TravakoJobEventRepository,
    ): JobEventStore = JobEventStoreImpl(travakoJobEventRepository)
}