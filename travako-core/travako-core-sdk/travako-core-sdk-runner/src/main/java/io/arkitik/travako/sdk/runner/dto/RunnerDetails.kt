package io.arkitik.travako.sdk.runner.dto

import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 31 12:26 AM, **Fri, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class RunnerDetails(
    val runnerKey: String,
    val runnerHost: String,
    val isRunning: Boolean,
    val isLeader: Boolean,
    val lastHeartbeatTime: LocalDateTime?,
)
