package io.arkitik.travako.adapter.exposed.job.event

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.adapter.exposed.job.event.creator.JobEventCreatorImpl
import io.arkitik.travako.adapter.exposed.job.event.query.JobEventStoreQueryImpl
import io.arkitik.travako.adapter.exposed.job.event.updater.JobEventUpdaterImpl
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.entity.exposed.job.event.TravakoJobEvent
import io.arkitik.travako.entity.exposed.job.event.TravakoJobEventTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.event.JobEventStore
import io.arkitik.travako.store.job.event.creator.JobEventCreator
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery
import io.arkitik.travako.store.job.event.updater.JobEventUpdater
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.exists
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.stringLiteral
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:50 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, JobEventDomain, TravakoJobEventTable>(
    identityTable = TravakoJobEventTable(travakoExposedNamingStrategy),
    database = database
), JobEventStore {
    private fun JobEventDomain.map() = this as TravakoJobEvent

    override val storeQuery: JobEventStoreQuery =
        JobEventStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): JobEventCreator =
        JobEventCreatorImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun JobEventDomain.identityUpdater(): JobEventUpdater =
        JobEventUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: JobEventDomain) {
        this[identityTable.job] = identity.map().jobInstanceUuid
        this[identityTable.eventType] = identity.eventType
        this[identityTable.processedFlag] = identity.processedFlag
    }

    override fun deleteAllByServerAndProcessed(server: ServerDomain) {
        ensureInTransaction(database) {
            val jobInstanceTable = identityTable.jobInstanceTable
            identityTable.deleteWhere {
                identityTable.processedFlag.eq(true)
                    .and {
                        exists(
                            jobInstanceTable
                                .select(stringLiteral("1"))
                                .where {
                                    jobInstanceTable.server.eq(server.uuid)
                                        .and(jobInstanceTable.uuid.eq(identityTable.job))
                                }
                        )
                    }
            }
        }
    }
}
