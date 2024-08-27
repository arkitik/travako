package io.arkitik.travako.starter.processor.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import java.net.InetAddress
import java.time.Duration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 2:41 PM, 27/08/2024
 */
@ConfigurationProperties(prefix = "arkitik.travako.config.runner")
class TravakoRunnerConfig(
    val key: String,
    @DefaultValue("5s") val heartbeat: Duration,
    @DefaultValue("30s") val jobsEvent: Duration,
    @DefaultValue("false") val duplicationDetection: Boolean,
) {
    val host: String = InetAddress.getLocalHost().toString()
}
