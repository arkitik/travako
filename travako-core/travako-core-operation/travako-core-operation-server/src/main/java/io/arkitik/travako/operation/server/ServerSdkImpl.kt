package io.arkitik.travako.operation.server

import io.arkitik.travako.operation.server.operation.RegisterServerOperationProvider
import io.arkitik.travako.operation.server.roles.IsServerRegisteredRole
import io.arkitik.travako.sdk.server.ServerSdk
import io.arkitik.travako.store.server.ServerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:38 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerSdkImpl(
    serverStore: ServerStore,
) : ServerSdk {
    override val registerServer =
        RegisterServerOperationProvider(serverStore = serverStore).registerServer
    override val isServerRegistered =
        IsServerRegisteredRole(serverStore.storeQuery)
}
