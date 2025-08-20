package io.arkitik.travako.entity.exposed.runner

import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.server.TravakoServerTable
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:06 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
data class TravakoSchedulerRunner(
    override val runnerKey: String,
    override var instanceState: InstanceState,
    override val runnerHost: String,
    override val uuid: String,
    val serverUuid: String,
    private val serverTable: TravakoServerTable,
    private val database: Database? = null,
    override val creationDate: LocalDateTime = LocalDateTime.now(),
    override var lastHeartbeatTime: LocalDateTime? = null,
) : SchedulerRunnerDomain {
    override val server: ServerDomain by lazy {
        serverTable.findIdentityByUuid(serverUuid, database)!!
    }
}
