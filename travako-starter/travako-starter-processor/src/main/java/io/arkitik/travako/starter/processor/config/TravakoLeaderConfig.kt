package io.arkitik.travako.starter.processor.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import java.time.Duration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 2:41 PM, 27/08/2024
 */
@ConfigurationProperties(prefix = "arkitik.travako.config.leader")
class TravakoLeaderConfig(
    @DefaultValue("1m") val switch: Duration,
    @DefaultValue("30s") val jobsAssignee: Duration,
)
