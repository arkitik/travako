package io.arkitik.travako.entity.exposed.server

import io.arkitik.travako.core.domain.server.ServerDomain
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class TravakoServer(
    override val uuid: String,
    override val serverKey: String,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : ServerDomain
