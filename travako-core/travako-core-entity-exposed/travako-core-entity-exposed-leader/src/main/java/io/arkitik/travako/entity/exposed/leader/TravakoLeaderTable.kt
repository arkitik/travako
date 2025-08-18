package io.arkitik.travako.entity.exposed.leader

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

class TravakoLeaderTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, LeaderDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoLeader::class)
) {
    val runnerTable = TravakoSchedulerRunnerTable(travakoExposedNamingStrategy)
    val serverTable = TravakoServerTable(travakoExposedNamingStrategy)


    override val uuid = varchar("uuid", 255)
    val runner = reference(
        name = "runner_uuid",
        refColumn = runnerTable.uuid,
        fkName = "travako_leader_runner_fk"
    )
    val server = reference(
        name = "server_uuid",
        refColumn = serverTable.uuid,
        fkName = "travako_leader_server_fk"
    )
    val lastModifiedDate = datetime("last_modified_date")

    init {
        uniqueIndex("travako_leader_server_unique", server)
    }

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): LeaderDomain {
        return TravakoLeader(
            uuid = resultRow[uuid],
            runnerUuid = resultRow[runner],
            runnerTable = runnerTable,
            serverUuid = resultRow[server],
            serverTable = serverTable,
            database = database,
            lastModifiedDate = resultRow[lastModifiedDate],
            creationDate = resultRow[creationDate]
        )
    }
}
