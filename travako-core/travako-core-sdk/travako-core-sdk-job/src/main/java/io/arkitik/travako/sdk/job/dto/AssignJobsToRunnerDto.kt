package io.arkitik.travako.sdk.job.dto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:09 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class AssignJobsToRunnerDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val jobKeys: List<String>,
)
