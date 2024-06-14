package io.arkitik.travako.starter.processor.config

import io.arkitik.travako.sdk.runner.dto.RunnerKeyDto
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import java.net.InetAddress
import java.time.Duration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:43 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@ConfigurationProperties(prefix = "arkitik.travako.config")
data class TravakoConfig(
    val serverKey: String,
    val runnerKey: String,
    @DefaultValue("5s") val heartbeat: Duration,
    @DefaultValue("1m") val leaderSwitch: Duration,
    @DefaultValue("30s") val jobsAssignee: Duration,
    @DefaultValue("30s") val jobsEvent: Duration,
    @DefaultValue("false") val duplicationProcessor: Boolean,
) {
    val keyDto: RunnerKeyDto =
        RunnerKeyDto(
            serverKey = serverKey,
            runnerKey = runnerKey,
            runnerHost = InetAddress.getLocalHost().toString()
        )

    fun isSelf(
        runnerKey: String,
        runnerHost: String,
    ): Boolean {
        return runnerKey == keyDto.runnerKey && runnerHost == keyDto.runnerHost
    }
}
