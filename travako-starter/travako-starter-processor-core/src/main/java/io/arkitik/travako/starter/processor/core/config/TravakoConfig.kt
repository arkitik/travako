package io.arkitik.travako.starter.processor.core.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:43 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@ConfigurationProperties(prefix = "arkitik.travako.config")
data class TravakoConfig(
    val serverKey: String,
)
