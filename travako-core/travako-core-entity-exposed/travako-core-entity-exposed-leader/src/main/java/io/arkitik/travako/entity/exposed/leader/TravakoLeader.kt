package io.arkitik.travako.entity.exposed.leader

import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 8:00 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class TravakoLeader(
    override val uuid: String,
    var runnerUuid: String,
    private val runnerTable: TravakoSchedulerRunnerTable?,
    val serverUuid: String,
    private val serverTable: TravakoServerTable,
    private val database: Database? = null,
    override var lastModifiedDate: LocalDateTime = LocalDateTime.now(),
    override val creationDate: LocalDateTime = LocalDateTime.now(),
) : LeaderDomain {
    override val runner: SchedulerRunnerDomain by lazy {
        runnerTable?.findIdentityByUuid(runnerUuid, database)!!
    }
    override val server: ServerDomain by lazy {
        serverTable.findIdentityByUuid(serverUuid, database)!!
    }
}
