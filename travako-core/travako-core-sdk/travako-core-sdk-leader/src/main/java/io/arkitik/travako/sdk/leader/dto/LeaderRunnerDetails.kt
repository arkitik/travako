package io.arkitik.travako.sdk.leader.dto

import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 10:41 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class LeaderRunnerDetails(
    val serverKey: String,
    val runnerKey: String,
    val lastHeartbeat: LocalDateTime?,
    val isRunning: Boolean,
)
