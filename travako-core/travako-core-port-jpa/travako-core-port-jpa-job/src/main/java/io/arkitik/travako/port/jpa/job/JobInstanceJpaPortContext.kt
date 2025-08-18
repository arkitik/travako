package io.arkitik.travako.port.jpa.job

import io.arkitik.travako.adapter.job.JobInstanceParamStoreImpl
import io.arkitik.travako.adapter.job.JobInstanceStoreImpl
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceParamRepository
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
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
class JobInstanceJpaPortContext {
    @Bean
    fun jobInstanceStore(
        travakoJobInstanceRepository: TravakoJobInstanceRepository,
    ): JobInstanceStore = JobInstanceStoreImpl(travakoJobInstanceRepository)

    @Bean
    fun jobInstanceParamStore(
        travakoJobInstanceParamRepository: TravakoJobInstanceParamRepository,
    ): JobInstanceParamStore = JobInstanceParamStoreImpl(travakoJobInstanceParamRepository)
}