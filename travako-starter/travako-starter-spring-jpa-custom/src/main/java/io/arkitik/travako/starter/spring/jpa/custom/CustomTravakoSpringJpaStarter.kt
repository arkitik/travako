package io.arkitik.travako.starter.spring.jpa.custom

import com.zaxxer.hikari.HikariDataSource
import io.arkitik.travako.adapter.job.event.repository.TravakoJobEventRepository
import io.arkitik.travako.adapter.job.event.repository.TravakoRunnerJobEventStateRepository
import io.arkitik.travako.adapter.job.repository.TravakoJobInstanceRepository
import io.arkitik.travako.adapter.leader.LeaderPortContext
import io.arkitik.travako.adapter.leader.repository.TravakoLeaderRepository
import io.arkitik.travako.adapter.runner.repository.TravakoSchedulerRunnerRepository
import io.arkitik.travako.adapter.server.repository.TravakoServerRepository
import io.arkitik.travako.entity.job.TravakoJobInstance
import io.arkitik.travako.entity.job.event.TravakoJobEvent
import io.arkitik.travako.entity.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.entity.leader.TravakoLeader
import io.arkitik.travako.entity.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.server.TravakoServer
import io.arkitik.travako.port.job.JobInstancePortContext
import io.arkitik.travako.port.job.event.JobEventPortContext
import io.arkitik.travako.port.runner.RunnerPortContext
import io.arkitik.travako.port.server.ServerPortContext
import io.arkitik.travako.port.shared.SharedPortContext
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
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
    LeaderPortContext::class,
    JobEventPortContext::class,
])
class CustomTravakoSpringJpaStarter {
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
    fun travakoAppDataSource(
        travakoDataSourceProperties: DataSourceProperties,
    ): DataSource = travakoDataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    @ConditionalOnMissingBean
    fun jpaVendorAdapter(
        travakoJpaProperties: JpaProperties,
    ): JpaVendorAdapter {
        val adapter: AbstractJpaVendorAdapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(travakoJpaProperties.isShowSql)
        if (travakoJpaProperties.database != null) {
            adapter.setDatabase(travakoJpaProperties.database)
        }
        if (travakoJpaProperties.databasePlatform != null) {
            adapter.setDatabasePlatform(travakoJpaProperties.databasePlatform)
        }
        adapter.setGenerateDdl(travakoJpaProperties.isGenerateDdl)
        return adapter
    }

    @Bean
    fun entityManagerFactoryBuilder(
        jpaVendorAdapter: JpaVendorAdapter,
        travakoJpaProperties: JpaProperties,
        persistenceUnitManager: ObjectProvider<PersistenceUnitManager?>,
        customizers: ObjectProvider<EntityManagerFactoryBuilderCustomizer>,
    ): EntityManagerFactoryBuilder? {
        val builder = EntityManagerFactoryBuilder(jpaVendorAdapter,
            travakoJpaProperties.properties, persistenceUnitManager.ifAvailable)
        customizers.orderedStream().forEach { customizer: EntityManagerFactoryBuilderCustomizer ->
            customizer.customize(builder)
        }
        return builder
    }

    @Bean
    @Primary
    fun travakoEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        travakoAppDataSource: DataSource,
        travakoJpaProperties: JpaProperties,
        travakoHibernateProperties: HibernateProperties,
    ): LocalContainerEntityManagerFactoryBean =
        builder.dataSource(travakoAppDataSource)
            .properties(
                travakoHibernateProperties.determineHibernateProperties(
                    travakoJpaProperties.properties,
                    HibernateSettings().ddlAuto {
                        travakoHibernateProperties.ddlAuto
                    }
                )
            )
            .packages(
                "io.arkitik.travako.entity.runner",
                "io.arkitik.travako.entity.job",
                "io.arkitik.travako.entity.job.event",
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

    @Bean
    fun travakoRunnerJobEventStateRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoRunnerJobEventState, String, TravakoRunnerJobEventStateRepository>(
        travakoEntityManagerFactory)

    @Bean
    fun travakoJobEventRepository(
        travakoEntityManagerFactory: EntityManager,
    ) = createRepository<TravakoJobEvent, String, TravakoJobEventRepository>(travakoEntityManagerFactory)
}
