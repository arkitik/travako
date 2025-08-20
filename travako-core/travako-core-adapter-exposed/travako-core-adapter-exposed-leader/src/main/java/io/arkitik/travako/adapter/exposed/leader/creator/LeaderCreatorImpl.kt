package io.arkitik.travako.adapter.exposed.leader.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.leader.LeaderDomain
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.leader.TravakoLeader
import io.arkitik.travako.entity.exposed.leader.TravakoLeaderTable
import io.arkitik.travako.store.leader.creator.LeaderCreator
import org.jetbrains.exposed.sql.Database
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28 12:57 AM, **Tue, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class LeaderCreatorImpl(
    private val identityTable: TravakoLeaderTable,
    private val database: Database? = null,
) : LeaderCreator {
    private lateinit var server: ServerDomain
    private lateinit var runner: SchedulerRunnerDomain
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun ServerDomain.server(): LeaderCreator {
        this@LeaderCreatorImpl.server = this
        return this@LeaderCreatorImpl
    }

    override fun SchedulerRunnerDomain.runner(): LeaderCreator {
        this@LeaderCreatorImpl.runner = this
        return this@LeaderCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, LeaderDomain> {
        this@LeaderCreatorImpl.uuid = this
        return this@LeaderCreatorImpl
    }

    override fun create(): LeaderDomain {
        return TravakoLeader(
            uuid = uuid,
            runnerUuid = runner.uuid,
            runnerTable = identityTable.runnerTable,
            serverUuid = server.uuid,
            serverTable = identityTable.serverTable,
            database = database
        )
    }
}
