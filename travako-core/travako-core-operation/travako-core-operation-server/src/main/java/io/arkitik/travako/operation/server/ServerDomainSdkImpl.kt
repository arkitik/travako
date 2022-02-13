package io.arkitik.travako.operation.server

import io.arkitik.travako.operation.server.operation.FetchServerOperationProvider
import io.arkitik.travako.sdk.domain.server.ServerDomainSdk
import io.arkitik.travako.store.server.ServerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:57 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerDomainSdkImpl(
    serverStore: ServerStore,
) : ServerDomainSdk {
    override val fetchServer =
        FetchServerOperationProvider(serverStore.storeQuery).fetchServer
}
