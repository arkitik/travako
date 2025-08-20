package io.arkitik.travako.starter.spring.jpa

import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.port.jpa.job.JobInstanceJpaPortContext
import io.arkitik.travako.port.jpa.job.event.JobEventJpaPortContext
import io.arkitik.travako.port.jpa.leader.LeaderJpaPortContext
import io.arkitik.travako.port.jpa.runner.RunnerJpaPortContext
import io.arkitik.travako.port.jpa.server.ServerJpaPortContext
import io.arkitik.travako.starter.spring.jpa.units.TravakoTransactionalExecutorImpl
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.PlatformTransactionManager

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@Import(
    value = [
        RunnerJpaPortContext::class,
        JobInstanceJpaPortContext::class,
        ServerJpaPortContext::class,
        LeaderJpaPortContext::class,
        JobEventJpaPortContext::class,
    ]
)
@EntityScan(
    value = [
        "io.arkitik.travako.entity.runner",
        "io.arkitik.travako.entity.job",
        "io.arkitik.travako.entity.job.event",
        "io.arkitik.travako.entity.server",
        "io.arkitik.travako.entity.leader",
    ]
)
@EnableJpaRepositories(
    value = [
        "io.arkitik.travako.adapter.runner.repository",
        "io.arkitik.travako.adapter.job.repository",
        "io.arkitik.travako.adapter.job.event.repository",
        "io.arkitik.travako.adapter.server.repository",
        "io.arkitik.travako.adapter.leader.repository",
    ]
)
class DefaultTravakoSpringJpaStarter {

    @Bean
    fun travakoTransactionalExecutor(
        transactionManager: PlatformTransactionManager,
    ): TravakoTransactionalExecutor = TravakoTransactionalExecutorImpl(transactionManager)
}
