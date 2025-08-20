package io.arkitik.travako.starter.spring.redis

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.port.redis.job.JobInstanceRedisPortContext
import io.arkitik.travako.port.redis.job.event.JobEventRedisPortContext
import io.arkitik.travako.port.redis.leader.LeaderRedisPortContext
import io.arkitik.travako.port.redis.runner.RunnerRedisPortContext
import io.arkitik.travako.port.redis.server.ServerRedisPortContext
import io.arkitik.travako.starter.spring.redis.units.TravakoTransactionalExecutorImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@Import(
    value = [
        RunnerRedisPortContext::class,
        JobInstanceRedisPortContext::class,
        ServerRedisPortContext::class,
        LeaderRedisPortContext::class,
        JobEventRedisPortContext::class,
    ]
)
@EnableRedisRepositories(
    value = [
        "io.arkitik.travako.adapter.redis.runner.repository",
        "io.arkitik.travako.adapter.redis.job.repository",
        "io.arkitik.travako.adapter.redis.job.event.repository",
        "io.arkitik.travako.adapter.redis.server.repository",
        "io.arkitik.travako.adapter.redis.leader.repository",
    ]
)
class DefaultTravakoSpringRediStarter {
    @Bean
    fun travakoTransactionalExecutor(): TravakoTransactionalExecutor = TravakoTransactionalExecutorImpl
}