package io.arkitik.travako.sdk.job.dto

import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30 11:53 PM, **Thu, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class JobDetails(
    val jobKey: String,
    val isRunning: Boolean,
    val jobTrigger: String,
    val isDuration: Boolean,
    val lastRunningTime: LocalDateTime?,
)
