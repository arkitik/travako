package io.arkitik.travako.starter.spring.exposed.custom

import com.zaxxer.hikari.HikariDataSource
import io.arkitik.travako.adapter.exposed.job.JobInstanceParamStoreImpl
import io.arkitik.travako.adapter.exposed.job.JobInstanceStoreImpl
import io.arkitik.travako.adapter.exposed.job.event.JobEventStoreImpl
import io.arkitik.travako.adapter.exposed.job.event.RunnerJobEventStateStoreImpl
import io.arkitik.travako.adapter.exposed.leader.LeaderStoreImpl
import io.arkitik.travako.adapter.exposed.runner.SchedulerRunnerStoreImpl
import io.arkitik.travako.adapter.exposed.server.ServerStoreImpl
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.starter.spring.exposed.custom.units.TravakoTransactionalExecutorImpl
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.JobInstanceStore
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.RunnerJobEventStateStore
import io.arkitik.travako.store.leader.LeaderStore
import io.arkitik.travako.store.runner.SchedulerRunnerStore
import io.arkitik.travako.store.server.ServerStore
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class CustomTravakoSpringExposedStarter {
    @Bean
    @ConditionalOnMissingBean
    fun databaseConfig() =
        DatabaseConfig {
            useNestedTransactions = true
        }

    @Bean
    @ConfigurationProperties("arkitik.travako.database.exposed.datasource")
    fun travakoExposedDataSourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    fun travakoExposedAppDataSource(
        @Qualifier("travakoExposedDataSourceProperties")
        travakoExposedDataSourceProperties: DataSourceProperties,
    ): DataSource = travakoExposedDataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    fun travakoExposedAppDatabase(
        @Qualifier("travakoExposedAppDataSource")
        datasource: DataSource,
        databaseConfig: DatabaseConfig,
    ): Database {
        return Database.connect(
            datasource = datasource,
            databaseConfig = databaseConfig
        )
    }

    @Bean
    fun jobInstanceStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobInstanceStore = JobInstanceStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun jobInstanceParamStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobInstanceParamStore = JobInstanceParamStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun runnerJobEventStateStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): RunnerJobEventStateStore = RunnerJobEventStateStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun jobEventStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): JobEventStore = JobEventStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun leaderStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): LeaderStore = LeaderStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun schedulerRunnerStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): SchedulerRunnerStore = SchedulerRunnerStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun serverStore(
        @Qualifier("travakoExposedAppDatabase") database: Database,
        travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
    ): ServerStore = ServerStoreImpl(
        database = database,
        travakoExposedNamingStrategy = travakoExposedNamingStrategy
    )

    @Bean
    fun travakoTransactionalExecutor(
        @Qualifier("travakoExposedAppDatabase") database: Database,
    ): TravakoTransactionalExecutor = TravakoTransactionalExecutorImpl(database)
}