package io.arkitik.travako.starter.spring.jpa.custom

import com.zaxxer.hikari.HikariDataSource
import io.arkitik.travako.function.transaction.TravakoTransactionalExecutor
import io.arkitik.travako.port.jpa.job.JobInstanceJpaPortContext
import io.arkitik.travako.port.jpa.job.event.JobEventJpaPortContext
import io.arkitik.travako.port.jpa.leader.LeaderJpaPortContext
import io.arkitik.travako.port.jpa.runner.RunnerJpaPortContext
import io.arkitik.travako.port.jpa.server.ServerJpaPortContext
import io.arkitik.travako.starter.spring.jpa.custom.units.TravakoTransactionalExecutorImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 9:41 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
@EnableJpaRepositories(
    basePackages = ["io.arkitik.travako.adapter"],
    entityManagerFactoryRef = "travakoEntityManagerFactory",
    transactionManagerRef = "travakoTransactionManager"
)
@Import(
    value = [
        RunnerJpaPortContext::class,
        JobInstanceJpaPortContext::class,
        ServerJpaPortContext::class,
        LeaderJpaPortContext::class,
        JobEventJpaPortContext::class,
    ]
)
class CustomTravakoSpringJpaStarter {
    @Bean
    @ConfigurationProperties("arkitik.travako.database.datasource")
    fun travakoDataSourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    fun travakoAppDataSource(
        travakoDataSourceProperties: DataSourceProperties,
    ): DataSource = travakoDataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    @Primary
    fun travakoEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("travakoAppDataSource")
        travakoAppDataSource: DataSource,
        jpaProperties: JpaProperties,
        hibernateProperties: HibernateProperties,
    ): LocalContainerEntityManagerFactoryBean =
        builder.dataSource(travakoAppDataSource)
            .properties(
                hibernateProperties.determineHibernateProperties(
                    jpaProperties.properties,
                    HibernateSettings().ddlAuto {
                        hibernateProperties.ddlAuto
                    }
                )
            )
            .packages("io.arkitik.travako.entity")
            .build()

    @Bean
    @Primary
    fun travakoTransactionManager(
        travakoTransactionManager: LocalContainerEntityManagerFactoryBean,
    ): PlatformTransactionManager = JpaTransactionManager(travakoTransactionManager.getObject()!!)

    @Bean
    fun travakoTransactionalExecutor(
        transactionManager: PlatformTransactionManager,
    ): TravakoTransactionalExecutor = TravakoTransactionalExecutorImpl(transactionManager)
}
