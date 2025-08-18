package io.arkitik.travako.adapter.exposed.runner.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.core.domain.runner.embedded.InstanceState
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunner
import io.arkitik.travako.entity.exposed.runner.TravakoSchedulerRunnerTable
import io.arkitik.travako.store.runner.creator.SchedulerRunnerCreator
import org.jetbrains.exposed.sql.Database
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 25 8:20 PM, **Sat, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class SchedulerRunnerCreatorImpl(
    private val database: Database? = null,
    private val identityTable: TravakoSchedulerRunnerTable,
) : SchedulerRunnerCreator {
    private lateinit var runnerKey: String
    private lateinit var runnerHost: String
    private lateinit var server: ServerDomain
    private var instanceState: InstanceState = InstanceState.UP
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun String.runnerKey(): SchedulerRunnerCreator {
        this@SchedulerRunnerCreatorImpl.runnerKey = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun String.runnerHost(): SchedulerRunnerCreator {
        this@SchedulerRunnerCreatorImpl.runnerHost = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun InstanceState.instanceState(): SchedulerRunnerCreator {
        this@SchedulerRunnerCreatorImpl.instanceState = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun ServerDomain.server(): SchedulerRunnerCreator {
        this@SchedulerRunnerCreatorImpl.server = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, SchedulerRunnerDomain> {
        this@SchedulerRunnerCreatorImpl.uuid = this
        return this@SchedulerRunnerCreatorImpl
    }

    override fun create() =
        TravakoSchedulerRunner(
            runnerKey = runnerKey,
            instanceState = instanceState,
            runnerHost = runnerHost,
            uuid = uuid,
            serverUuid = server.uuid,
            serverTable = identityTable.serverTable,
            database = database
        )
}
