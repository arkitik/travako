package io.arkitik.travako.sdk.domain.server

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:13 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
interface ServerDomainSdk {
    val fetchServer: Operation<ServerDomainDto, ServerDomain>
}
