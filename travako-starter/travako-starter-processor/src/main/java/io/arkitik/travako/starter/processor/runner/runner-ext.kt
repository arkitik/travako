package io.arkitik.travako.starter.processor.runner

import io.arkitik.travako.sdk.runner.dto.RunnerDetails
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 01 11:52 AM, **Tue, March 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
fun RunnerDetails.heartbeatLessThanExpectedTime(lastHeartbeatSeconds: Long) =
    lastHeartbeatTime?.let {
        Duration.between(
            LocalDateTime.now(),
            it
        ).seconds.absoluteValue > lastHeartbeatSeconds
    } ?: true
