package io.arkitik.travako.starter.startup.processor.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import java.time.Duration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:43 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@ConfigurationProperties(prefix = "travako.config")
@ConstructorBinding
data class TravakoConfig(
    val serverKey: String,
    val runnerKey: String,
    @DefaultValue("5s") val heartbeat: Duration,
    @DefaultValue("1m") val leaderSwitch: Duration,
    @DefaultValue("1.5") val recoveringMultiplier: Double,
    @DefaultValue("30s") val jobsAssignee: Duration,
)
