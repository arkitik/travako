package io.arkitik.travako.entity.exposed.job.event

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow

class TravakoRunnerJobEventStateTable(
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : RadixTable<String, RunnerJobEventStateDomain>(
    travakoExposedNamingStrategy.provideTableName(TravakoRunnerJobEventState::class)
) {
    val runnerTable = TravakoSchedulerRunnerTable(travakoExposedNamingStrategy)
    val jobEventTable = TravakoJobEventTable(travakoExposedNamingStrategy)

    override val uuid = varchar("uuid", 255)
    val runner = reference(
        name = "runner_uuid",
        refColumn = runnerTable.uuid,
        fkName = "travako_runner_job_event_state_runner_fk"
    )
    val jobEvent = reference(
        name = "job_event_uuid",
        refColumn = jobEventTable.uuid,
        fkName = "travako_runner_job_event_state_event_fk"
    )

    init {
        uniqueIndex(
            "travako_job_event_runner_processed_state_unique",
            runner,
            jobEvent
        )
    }

    override fun mapToIdentity(
        resultRow: ResultRow,
        database: Database?,
    ): RunnerJobEventStateDomain {
        return TravakoRunnerJobEventState(
            runnerUuid = resultRow[runner],
            jobEventUuid = resultRow[jobEvent],
            runnerTable = runnerTable,
            jobEventTable = jobEventTable,
            database = database,
            uuid = resultRow[uuid],
            creationDate = resultRow[creationDate]
        )
    }
}

