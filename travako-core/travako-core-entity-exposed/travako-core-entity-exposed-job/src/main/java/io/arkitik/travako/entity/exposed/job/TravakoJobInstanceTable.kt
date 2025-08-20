package io.arkitik.travako.entity.exposed.job

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

class TravakoJobInstanceTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, JobInstanceDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoJobInstance::class)
) {
    val serverTable = TravakoServerTable(travakoExposedNamingStrategy)
    val runnerTable = TravakoSchedulerRunnerTable(travakoExposedNamingStrategy)

    override val uuid = varchar("uuid", 255)
    val jobKey = varchar("job_key", 255)
    val jobClassName = varchar("job_class_name", 255)
    val jobStatus = enumerationByName("job_status", 50, JobStatus::class)
    val server = reference(
        name = "server_uuid",
        refColumn = serverTable.uuid,
        fkName = "travako_job_instance_server_fk"
    )

    val jobTrigger = varchar("job_trigger", 255)
    val jobTriggerType = enumerationByName<JobInstanceTriggerType>("job_trigger_type", 255)
    val assignedTo = reference(
        name = "runner_uuid",
        refColumn = runnerTable.uuid,
        fkName = "travako_job_instance_runner_fk"
    ).nullable()

    val lastRunningTime = datetime("last_running_time").nullable()
    val nextExecutionTime = datetime("next_execution_time").nullable()
    val singleRun = bool("single_run")

    init {
        uniqueIndex("travako_job_instance_key_server_unique", jobKey, server)
    }

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): JobInstanceDomain {
        return TravakoJobInstance(
            uuid = resultRow[uuid],
            jobKey = resultRow[jobKey],
            jobClassName = resultRow[jobClassName],
            jobStatus = resultRow[jobStatus],
            serverUuid = resultRow[server],
            serverTable = serverTable,
            database = database,
            creationDate = resultRow[creationDate],
            runnerTable = runnerTable,
            jobTrigger = resultRow[jobTrigger],
            jobTriggerType = resultRow[jobTriggerType],
            assignedToUuid = resultRow[assignedTo],
            lastRunningTime = resultRow[lastRunningTime],
            nextExecutionTime = resultRow[nextExecutionTime],
            singleRun = resultRow[singleRun],
        )
    }
}
