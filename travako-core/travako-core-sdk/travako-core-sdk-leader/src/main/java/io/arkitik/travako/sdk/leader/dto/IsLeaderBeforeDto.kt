package io.arkitik.travako.sdk.leader.dto

import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 5:10 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class IsLeaderBeforeDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val dateBefore: LocalDateTime,
)
