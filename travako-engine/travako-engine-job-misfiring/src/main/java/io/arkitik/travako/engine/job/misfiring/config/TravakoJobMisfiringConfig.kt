package io.arkitik.travako.engine.job.misfiring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 9:07 PM, 25/08/2024
 */
@ConfigurationProperties(prefix = "arkitik.travako.config.misfiring")
class TravakoJobMisfiringConfig(
    @DefaultValue("true") val enabled: Boolean,
    @DefaultValue("1") val runIntervalMinutes: Long,
    @DefaultValue("10") val thresholdSeconds: Long,
)
