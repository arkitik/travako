package io.arkitik.travako.operation.server.operation

import io.arkitik.radix.develop.operation.ext.operationBuilder
import io.arkitik.radix.develop.shared.ext.unprocessableEntity
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.operation.server.errors.ServerErrors
import io.arkitik.travako.sdk.domain.server.dto.ServerDomainDto
import io.arkitik.travako.store.server.query.ServerStoreQuery

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 6:57 PM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class FetchServerOperationProvider(
    private val serverStoreQuery: ServerStoreQuery,
) {
    val fetchServer = operationBuilder<ServerDomainDto, ServerDomain> {
        mainOperation {
            with(serverStoreQuery) {
                findByServerKey(serverKey) ?: throw ServerErrors.SERVER_NOT_REGISTERED.unprocessableEntity()
            }
        }
    }
}
