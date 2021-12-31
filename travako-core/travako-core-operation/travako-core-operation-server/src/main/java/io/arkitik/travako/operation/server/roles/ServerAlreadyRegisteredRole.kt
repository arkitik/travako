package io.arkitik.travako.operation.server.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.operation.server.errors.ServerErrors
import io.arkitik.travako.sdk.server.dto.ServerKeyDto
import io.arkitik.travako.store.server.query.ServerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 4:38 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class ServerAlreadyRegisteredRole(
    private val serverStoreQuery: ServerStoreQuery,
) : OperationRole<ServerKeyDto, Unit> {
    override fun ServerKeyDto.operateRole() {
        with(serverStoreQuery) {
            existsByServerKey(
                serverKey
            ).takeIf { it }?.also {
                throw ServerErrors.SERVER_ALREADY_REGISTERED.unprocessableEntity()
            }
        }
    }

}
