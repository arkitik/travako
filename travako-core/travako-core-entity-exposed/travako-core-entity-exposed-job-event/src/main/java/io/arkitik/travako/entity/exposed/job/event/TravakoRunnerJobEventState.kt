package io.arkitik.travako.entity.exposed.job.event

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 03 11:42 PM, **Mon, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */

data class TravakoRunnerJobEventState(
    override val uuid: String,
    override val creationDate: LocalDateTime,
    val runnerUuid: String,
    val jobEventUuid: String,
    private val runnerTable: TravakoSchedulerRunnerTable,
    private val jobEventTable: TravakoJobEventTable,
    private val database: Database?,
) : RunnerJobEventStateDomain {
    override val jobEvent: JobEventDomain by lazy {
        jobEventTable.findIdentityByUuid(jobEventUuid, database)!!
    }

    override val runner: SchedulerRunnerDomain by lazy {
        runnerTable.findIdentityByUuid(runnerUuid, database)!!
    }
}