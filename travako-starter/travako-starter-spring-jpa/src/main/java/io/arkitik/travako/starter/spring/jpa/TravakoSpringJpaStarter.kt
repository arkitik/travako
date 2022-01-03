package io.arkitik.travako.starter.spring.jpa

import com.zaxxer.hikari.HikariDataSource
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.adapter.leader.LeaderPortContext
import io.arkitik.travako.adapter.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.adapter.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.adapter.server.repository.TravakoServerRepository
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.port.job.JobInstancePortContext
import io.arkitik.travako.port.runner.RunnerPortContext
import io.arkitik.travako.port.server.ServerPortContext
import io.arkitik.travako.port.shared.SharedPortContext
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.SchemaManagementProvider
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManager
import javax.sql.DataSource

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@Import(value = [
    SharedPortContext::class,
    RunnerPortContext::class,
    JobInstancePortContext::class,
    ServerPortContext::class,
    LeaderPortContext::class
])
class TravakoSpringJpaStarter {
    @Bean
    @Primary
    @ConfigurationProperties("arkitik.travako.database.datasource")
    fun travakoDataSourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    @ConfigurationProperties("arkitik.travako.database.init")
    fun travakoSqlInitializationProperties() = SqlInitializationProperties()

    @Bean
    @Primary
    @ConfigurationProperties("arkitik.travako.database.hibernate")
    fun travakoHibernateProperties() = HibernateProperties()

    @Bean
    @Primary
    @ConfigurationProperties("arkitik.travako.database.jpa")
    fun travakoJpaProperties() = JpaProperties()

    @Bean
    @Primary
    fun travakoAppDataSource(travakoDataSourceProperties: DataSourceProperties): DataSource {
        return travakoDataSourceProperties.initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    @Primary
    fun travakoEntityManagerFactory(
        providers: ObjectProvider<SchemaManagementProvider>,
        builder: EntityManagerFactoryBuilder,
        travakoAppDataSource: DataSource,
        travakoJpaProperties: JpaProperties,
        travakoHibernateProperties: HibernateProperties,
    ): LocalContainerEntityManagerFactoryBean =
        builder.dataSource(travakoAppDataSource)
            .properties(
                travakoHibernateProperties.determineHibernateProperties(
                    travakoJpaProperties.properties,
                    HibernateSettings()
                )
            )
            .packages(
                "io.arkitik.travako.entity.runner",
                "io.arkitik.travako.entity.job",
                "io.arkitik.travako.entity.server",
                "io.arkitik.travako.entity.leader",
            ).build()

    @Bean
    @Primary
    fun travakoTransactionManager(
        travakoTransactionManager: LocalContainerEntityManagerFactoryBean,
    ): PlatformTransactionManager = JpaTransactionManager(travakoTransactionManager.getObject()!!)

    @Bean
    fun travakoLeaderRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoLeader, String, TravakoLeaderRepository>(travakoEntityManagerFactory)

    @Bean
    fun travakoServerRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoServer, String, TravakoServerRepository>(travakoEntityManagerFactory)

    @Bean
    fun travakoJobInstanceRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoJobInstance, String, TravakoJobInstanceRepository>(travakoEntityManagerFactory)

    @Bean
    fun travakoSchedulerRunnerRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoSchedulerRunner, String, TravakoSchedulerRunnerRepository>(travakoEntityManagerFactory)
}
