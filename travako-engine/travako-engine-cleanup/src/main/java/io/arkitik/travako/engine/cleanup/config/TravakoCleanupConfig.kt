package io.arkitik.travako.engine.cleanup.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * @author Ibrahim Al-Tamimi 
 * @since 11:46, Tuesday, 31/03/2026
 **/
@ConfigurationProperties(prefix = "arkitik.travako.config.cleanup")
class TravakoCleanupConfig(
    @DefaultValue("true") val enabled: Boolean = true,
    @DefaultValue("24") val runIntervalHours: Long = 24,
    @DefaultValue("2") val jobsAgeInDays: Long = 2,
)