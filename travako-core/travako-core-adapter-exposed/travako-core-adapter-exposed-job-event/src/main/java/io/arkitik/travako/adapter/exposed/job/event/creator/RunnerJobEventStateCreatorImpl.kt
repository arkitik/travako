package io.arkitik.travako.adapter.exposed.job.event.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventState
import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventStateTable
import io.arkitik.travako.store.job.event.creator.RunnerJobEventStateCreator
import org.jetbrains.exposed.sql.Database
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:07 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class RunnerJobEventStateCreatorImpl(
    private val identityTable: TravakoRunnerJobEventStateTable,
    private val database: Database? = null,
) : RunnerJobEventStateCreator {
    private lateinit var runner: SchedulerRunnerDomain
    private lateinit var jobEvent: JobEventDomain
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun SchedulerRunnerDomain.runner(): RunnerJobEventStateCreator {
        this@RunnerJobEventStateCreatorImpl.runner = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun JobEventDomain.jobEvent(): RunnerJobEventStateCreator {
        this@RunnerJobEventStateCreatorImpl.jobEvent = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, RunnerJobEventStateDomain> {
        this@RunnerJobEventStateCreatorImpl.uuid = this
        return this@RunnerJobEventStateCreatorImpl
    }

    override fun create() =
        TravakoRunnerJobEventState(
            uuid = uuid,
            runnerUuid = runner.uuid,
            runnerTable = identityTable.runnerTable,
            jobEventUuid = jobEvent.uuid,
            jobEventTable = identityTable.jobEventTable,
            database = database,
            creationDate = LocalDateTime.now()
        )
}
