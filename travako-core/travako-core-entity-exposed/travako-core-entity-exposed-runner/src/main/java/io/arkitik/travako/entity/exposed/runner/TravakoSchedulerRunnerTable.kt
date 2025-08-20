package io.arkitik.travako.entity.exposed.runner

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

class TravakoSchedulerRunnerTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, SchedulerRunnerDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoSchedulerRunner::class)
) {
    val serverTable = TravakoServerTable(travakoExposedNamingStrategy)

    override val uuid = varchar("uuid", 255)
    val runnerKey = varchar("runner_key", 255)
    val instanceState = enumerationByName("instance_state", 50, InstanceState::class)
    val runnerHost = varchar("runner_host", 255)
    val server = reference(
        name = "server_uuid",
        refColumn = serverTable.uuid,
        fkName = "travako_scheduler_runner_server_fk"
    )

    val lastHeartbeatTime = datetime("last_heartbeat_time")
        .nullable()

    init {
        uniqueIndex("travako_scheduler_runner_key_server_unique", runnerKey, server, runnerHost)
    }

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): SchedulerRunnerDomain {
        return TravakoSchedulerRunner(
            uuid = resultRow[uuid],
            runnerKey = resultRow[runnerKey],
            instanceState = resultRow[instanceState],
            runnerHost = resultRow[runnerHost],
            serverUuid = resultRow[server],
            serverTable = serverTable,
            database = database,
            creationDate = resultRow[creationDate]
        )
    }
}
