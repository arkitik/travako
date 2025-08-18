package io.arkitik.travako.entity.exposed.server

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 10:02 AM, 17/08/2025
 */
class TravakoServerTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, ServerDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoServer::class)
) {
    override val uuid = varchar("uuid", 255)
    val serverKey = varchar("server_key", 255)
        .uniqueIndex("travako_server_key_unique")

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): ServerDomain {
        return TravakoServer(
            uuid = resultRow[uuid],
            serverKey = resultRow[serverKey],
            creationDate = resultRow[creationDate]
        )
    }
}