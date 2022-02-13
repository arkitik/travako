package io.arkitik.travako.sdk.domain.job.dto

import io.arkitik.travako.core.domain.server.ServerDomain

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:10 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class JobDomainDto(
    val server: ServerDomain,
    val jobKey: String,
)
