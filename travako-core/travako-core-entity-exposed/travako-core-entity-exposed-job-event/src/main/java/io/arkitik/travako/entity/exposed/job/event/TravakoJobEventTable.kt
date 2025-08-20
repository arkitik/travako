package io.arkitik.travako.entity.exposed.job.event

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow

class TravakoJobEventTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, JobEventDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoJobEvent::class)
) {
    val jobInstanceTable = TravakoJobInstanceTable(travakoExposedNamingStrategy)

    override val uuid = varchar("uuid", 255)
    val job = reference(
        name = "job_uuid",
        refColumn = jobInstanceTable.uuid,
        fkName = "travako_job_event_instance_fk"
    )
    val eventType = enumerationByName("event_type", 50, JobEventType::class)
    val processedFlag = bool("processed_flag")

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): JobEventDomain {
        return TravakoJobEvent(
            uuid = resultRow[uuid],
            eventType = resultRow[eventType],
            processedFlag = resultRow[processedFlag],
            creationDate = resultRow[creationDate],
            jobInstanceUuid = resultRow[job],
            jobInstanceTable = jobInstanceTable,
            database = database,
        )
    }
}
