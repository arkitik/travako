package io.arkitik.travako.operation.server.roles

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.travako.sdk.server.dto.ServerKeyDto
import io.arkitik.travako.store.server.query.ServerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 8:47 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class IsServerRegisteredRole(
    private val serverStoreQuery: ServerStoreQuery,
) : OperationRole<ServerKeyDto, Boolean> {
    override fun ServerKeyDto.operateRole() =
        with(serverStoreQuery) {
            existsByServerKey(
                serverKey
            )
        }
}
