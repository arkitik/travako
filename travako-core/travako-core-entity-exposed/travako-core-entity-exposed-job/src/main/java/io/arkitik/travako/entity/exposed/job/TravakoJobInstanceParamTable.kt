package io.arkitik.travako.entity.exposed.job

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow

class TravakoJobInstanceParamTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, JobInstanceParamDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoJobInstanceParam::class)
) {
    val jobInstanceTable = TravakoJobInstanceTable(travakoExposedNamingStrategy)

    override val uuid = varchar("uuid", 255)
    val job = reference(
        name = "job_uuid",
        refColumn = jobInstanceTable.uuid,
        fkName = "travako_job_instance_param_job_fk"
    )
    val key = varchar("field_key", 255)
    val value = varchar("field_value", 255).nullable()

    init {
        uniqueIndex("travako_job_instance_param_key_unique", job, key)
    }

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): JobInstanceParamDomain {
        return TravakoJobInstanceParam(
            uuid = resultRow[uuid],
            jobUuid = resultRow[job],
            jobInstanceTable = jobInstanceTable,
            database = database,
            key = resultRow[key],
            value = resultRow[value],
            creationDate = resultRow[creationDate]
        )
    }
}
