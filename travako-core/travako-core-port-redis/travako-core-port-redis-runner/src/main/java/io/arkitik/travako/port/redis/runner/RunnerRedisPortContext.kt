package io.arkitik.travako.port.redis.runner

import io.arkitik.travako.adapter.redis.runner.SchedulerRunnerStoreImpl
import io.arkitik.travako.adapter.redis.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class RunnerRedisPortContext {
    @Bean
    fun schedulerRunnerStore(
        travakoSchedulerRunnerRepository: TravakoSchedulerRunnerRepository,
    ): SchedulerRunnerStore = SchedulerRunnerStoreImpl(
        travakoSchedulerRunnerRepository = travakoSchedulerRunnerRepository
    )
}