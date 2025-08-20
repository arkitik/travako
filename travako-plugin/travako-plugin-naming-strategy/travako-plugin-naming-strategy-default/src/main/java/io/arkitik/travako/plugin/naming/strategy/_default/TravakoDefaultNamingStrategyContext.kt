package io.arkitik.travako.plugin.naming.strategy._default

import io.arkitik.travako.plugin.naming.strategy._default.config.TravakoNamingStrategyConfig
import io.arkitik.travako.plugin.naming.strategy._default.functions.DefaultTravakoExposedNamingStrategy
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:49 PM, 18/08/2025
 */
@Configuration
@ConditionalOnMissingBean(TravakoExposedNamingStrategy::class)
@EnableConfigurationProperties(TravakoNamingStrategyConfig::class)
class TravakoDefaultNamingStrategyContext {
    @Bean
    fun defaultTravakoExposedNamingStrategy(
        travakoNamingStrategyConfig: TravakoNamingStrategyConfig,
    ): TravakoExposedNamingStrategy = DefaultTravakoExposedNamingStrategy(
        travakoNamingStrategyConfig = travakoNamingStrategyConfig
    )
}