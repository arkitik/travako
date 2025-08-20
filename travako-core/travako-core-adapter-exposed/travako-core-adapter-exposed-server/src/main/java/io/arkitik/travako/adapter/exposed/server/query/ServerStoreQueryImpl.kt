package io.arkitik.travako.adapter.exposed.server.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import io.arkitik.travako.store.server.query.ServerStoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 1:03 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class ServerStoreQueryImpl(
    database: Database?,
    travakoServerTable: TravakoServerTable,
) : ExposedStoreQuery<String, ServerDomain, TravakoServerTable>(
    identityTable = travakoServerTable,
    database = database
), ServerStoreQuery {
    override fun findByServerKey(
        serverKey: String,
    ): ServerDomain? {
        return ensureInTransaction(database) {
            identityTable.selectAll()
                .where {
                    identityTable.serverKey.eq(serverKey)
                }.singleOrNull()?.let { identityTable.mapToIdentity(it, database) }
        }
    }

    override fun existsByServerKey(
        serverKey: String,
    ) = ensureInTransaction(database) {
        identityTable.selectAll()
            .where {
                identityTable.serverKey.eq(serverKey)
            }.exist()
    }
}
