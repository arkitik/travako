package io.arkitik.travako.plugin.naming.strategy._default.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 8:04 PM, 18/08/2025
 */
@ConfigurationProperties(prefix = "arkitik.travako.naming-strategy")
class TravakoNamingStrategyConfig(
    val prefix: String? = null,
)