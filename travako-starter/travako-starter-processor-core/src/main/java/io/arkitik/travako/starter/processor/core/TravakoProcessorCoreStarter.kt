package io.arkitik.travako.starter.processor.core

import io.arkitik.travako.sdk.job.JobInstanceSdk
import io.arkitik.travako.starter.job.registry.JobInstancesRegistry
import io.arkitik.travako.starter.job.source.TravakoJobInstanceProvider
import io.arkitik.travako.starter.processor.core.config.TravakoConfig
import io.arkitik.travako.starter.processor.core.functions.JobInstancesRegistryImpl
import io.arkitik.travako.starter.processor.core.functions.TravakoJobInstanceProviderImpl
import io.arkitik.travako.starter.processor.core.units.DefaultTravakoJobInstanceProviderUnit
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 2:35 PM, 27/08/2024
 */
@Configuration
@EnableConfigurationProperties(TravakoConfig::class)
class TravakoProcessorCoreStarter {

    @Bean
    fun jobInstancesRegistryImpl(
        jobInstanceSdk: JobInstanceSdk,
        travakoConfig: TravakoConfig,
    ): JobInstancesRegistry = JobInstancesRegistryImpl(
        jobInstanceSdk = jobInstanceSdk,
        travakoConfig = travakoConfig
    )

    @Bean
    fun travakoJobInstanceProvider(
        providerUnits: List<TravakoJobInstanceProvider.ProviderUnit>,
    ): TravakoJobInstanceProvider =
        TravakoJobInstanceProviderImpl(providerUnits)

    @Bean
    fun defaultTravakoJobInstanceProviderUnit(
        defaultListableBeanFactory: DefaultListableBeanFactory,
    ): TravakoJobInstanceProvider.ProviderUnit =
        DefaultTravakoJobInstanceProviderUnit(defaultListableBeanFactory)
}
