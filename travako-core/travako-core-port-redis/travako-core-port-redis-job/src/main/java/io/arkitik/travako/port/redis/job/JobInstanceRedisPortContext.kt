package io.arkitik.travako.port.redis.job

import io.arkitik.travako.adapter.redis.job.JobInstanceParamStoreImpl
import io.arkitik.travako.adapter.redis.job.JobInstanceStoreImpl
import io.arkitik.travako.adapter.redis.job.repository.TravakoJobInstanceParamRepository
import io.arkitik.travako.adapter.redis.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:32 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class JobInstanceRedisPortContext {
    @Bean
    fun jobInstanceStore(
        travakoJobInstanceRepository: TravakoJobInstanceRepository,
    ): JobInstanceStore = JobInstanceStoreImpl(travakoJobInstanceRepository)

    @Bean
    fun jobInstanceParamStore(
        travakoJobInstanceParamRepository: TravakoJobInstanceParamRepository,
    ): JobInstanceParamStore = JobInstanceParamStoreImpl(travakoJobInstanceParamRepository)
}