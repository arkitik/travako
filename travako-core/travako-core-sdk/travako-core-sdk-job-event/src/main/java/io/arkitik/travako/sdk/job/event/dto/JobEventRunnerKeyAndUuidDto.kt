package io.arkitik.travako.sdk.job.event.dto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:57 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class JobEventRunnerKeyAndUuidDto(
    val serverKey: String,
    val runnerKey: String,
    val runnerHost: String,
    val eventUuid: String,
)
