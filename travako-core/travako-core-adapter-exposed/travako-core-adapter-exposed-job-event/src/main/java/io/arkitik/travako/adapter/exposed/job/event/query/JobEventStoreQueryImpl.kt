package io.arkitik.travako.adapter.exposed.job.event.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.entity.exposed.job.event.TravakoJobEventTable
import io.arkitik.travako.store.job.event.query.JobEventStoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 4:51 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobEventStoreQueryImpl(
    database: Database?,
    identityTable: TravakoJobEventTable,
) : ExposedStoreQuery<String, JobEventDomain, TravakoJobEventTable>(
    identityTable = identityTable,
    database = database
), JobEventStoreQuery {
    override fun findAllPendingEventsForServer(server: ServerDomain): List<JobEventDomain> =
        transaction(database) {
            val jobInstanceTable = identityTable.jobInstanceTable
            identityTable
                .innerJoin(jobInstanceTable)
                .selectAll()
                .where {
                    (jobInstanceTable.server eq server.uuid)
                        .and(identityTable.processedFlag eq false)
                }.orderBy(identityTable.creationDate)
                .map {
                    identityTable.mapToIdentity(it, database)
                }
        }
}
