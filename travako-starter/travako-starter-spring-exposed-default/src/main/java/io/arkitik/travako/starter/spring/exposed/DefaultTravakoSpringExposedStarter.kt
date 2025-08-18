package io.arkitik.travako.starter.spring.exposed

import io.arkitik.travako.adapter.exposed.server.ServerStoreImpl
import io.arkitik.travako.adapter.exposed.job.JobInstanceParamStoreImpl
import io.arkitik.travako.adapter.exposed.job.JobInstanceStoreImpl
import io.arkitik.travako.adapter.exposed.job.event.JobEventStoreImpl
import io.arkitik.travako.adapter.exposed.job.event.RunnerJobEventStateStoreImpl
import io.arkitik.travako.adapter.exposed.leader.LeaderStoreImpl
import io.arkitik.travako.adapter.exposed.runner.SchedulerRunnerStoreImpl
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore
import io.arkitik.travako.store.leader.LeaderStore
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import io.arkitik.travako.store.server.ServerStore
import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class DefaultTravakoSpringExposedStarter {

    @Bean
    fun jobInstanceStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobInstanceStore = JobInstanceStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun jobInstanceParamStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobInstanceParamStore = JobInstanceParamStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun runnerJobEventStateStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): RunnerJobEventStateStore = RunnerJobEventStateStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun jobEventStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobEventStore = JobEventStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun leaderStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): LeaderStore = LeaderStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun schedulerRunnerStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): SchedulerRunnerStore = SchedulerRunnerStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun serverStore(
        @Autowired(required = false) database: Database?,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): ServerStore = ServerStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )
}