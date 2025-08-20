package io.arkitik.travako.port.server

import io.arkitik.travako.operation.server.ServerDomainSdkImpl
import io.arkitik.travako.operation.server.ServerSdkImpl
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.store.server.ServerStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 9:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
@Configuration
class ServerPortContext {
    @Bean
    fun serverDomainSdk(
        serverStore: ServerStore,
    ): ServerDomainSdk = ServerDomainSdkImpl(serverStore)

    @Bean
    fun serverSdk(
        serverStore: ServerStore,
    ): ServerSdk = ServerSdkImpl(serverStore)
}
