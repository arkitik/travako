package io.arkitik.travako.operation.server.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.store.storeCreator
import io.arkitik.travako.operation.server.roles.ServerAlreadyRegisteredRole
import io.arkitik.travako.sdk.server.dto.ServerKeyDto
import io.arkitik.travako.store.server.ServerStore

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:40 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RegisterServerOperationProvider(
    private val serverStore: ServerStore,
) {
    val registerServer = operationBuilder<ServerKeyDto, Unit> {
        install(ServerAlreadyRegisteredRole(serverStore.storeQuery))
        mainOperation {
            with(serverStore) {
                storeCreator(identityCreator()) {
                    serverKey.serverKey()
                    create()
                }.save()
            }
        }
    }
}
