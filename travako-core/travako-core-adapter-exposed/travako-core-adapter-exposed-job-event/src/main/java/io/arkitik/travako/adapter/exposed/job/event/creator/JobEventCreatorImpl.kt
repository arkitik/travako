package io.arkitik.travako.adapter.exposed.job.event.creator

import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.embedded.JobEventType
import io.arkitik.travako.entity.exposed.job.event.TravakoJobEvent
import io.arkitik.travako.entity.exposed.job.event.TravakoJobEventTable
import io.arkitik.travako.store.job.event.creator.JobEventCreator
import org.jetbrains.exposed.sql.Database
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:52 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventCreatorImpl(
    private val identityTable: TravakoJobEventTable,
    private val database: Database? = null,
) : JobEventCreator {
    private lateinit var jobInstance: JobInstanceDomain
    private lateinit var eventType: JobEventType
    private var uuid: String = UUID.randomUUID().toString().replace("-", "")

    override fun JobInstanceDomain.jobInstance(): JobEventCreator {
        this@JobEventCreatorImpl.jobInstance = this
        return this@JobEventCreatorImpl
    }

    override fun JobEventType.eventType(): JobEventCreator {
        this@JobEventCreatorImpl.eventType = this
        return this@JobEventCreatorImpl
    }

    override fun String.uuid(): StoreIdentityCreator<String, JobEventDomain> {
        this@JobEventCreatorImpl.uuid = this
        return this@JobEventCreatorImpl
    }

    override fun create() =
        TravakoJobEvent(
            uuid = uuid,
            jobInstanceUuid = jobInstance.uuid,
            jobInstanceTable = identityTable.jobInstanceTable,
            eventType = eventType,
            database = database
        )
}
