package io.arkitik.travako.sdk.job.dto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 6:45 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class JobServerRunnerKeyDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val jobKey: String,
)
