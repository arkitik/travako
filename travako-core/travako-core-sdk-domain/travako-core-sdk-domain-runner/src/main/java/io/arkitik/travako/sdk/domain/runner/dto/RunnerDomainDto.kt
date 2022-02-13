package io.arkitik.travako.sdk.domain.runner.dto

import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:12 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class RunnerDomainDto(
    val server: ServerDomain,
    val runnerKey: String,
    val runnerHost: String,
)
