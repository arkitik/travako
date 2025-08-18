package io.arkitik.travako.entity.exposed.job

import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobInstanceTriggerType
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:19 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class TravakoJobInstance(
    override val jobKey: String,
    override var jobClassName: String,
    override var jobStatus: JobStatus,
    override val uuid: String,
    val serverUuid: String,
    private val serverTable: TravakoServerTable,
    override var jobTrigger: String,
    override var jobTriggerType: JobInstanceTriggerType,
    var assignedToUuid: String? = null,
    private val runnerTable: TravakoSchedulerRunnerTable?,
    private val database: Database?,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    override var lastRunningTime: LocalDateTime?,
    override var nextExecutionTime: LocalDateTime?,
    override var singleRun: Boolean,
) : JobInstanceDomain {
    override val server: ServerDomain by lazy {
        serverTable.findIdentityByUuid(serverUuid, database)!!
    }
    override val assignedTo: SchedulerRunnerDomain? by lazy {
        assignedToUuid?.let {
            runnerTable?.findIdentityByUuid(it, database)
        }
    }
}
