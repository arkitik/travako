package io.arkitik.travako.adapter.exposed.job

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.adapter.exposed.job.creator.JobInstanceCreatorImpl
import io.arkitik.travako.adapter.exposed.job.query.JobInstanceStoreQueryImpl
import io.arkitik.travako.adapter.exposed.job.updater.JobInstanceUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstance
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.JobInstanceStore
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceUpdater
import org.jetbrains.exposed.v1.core.OrOp
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import java.time.LocalDateTime

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, JobInstanceDomain, TravakoJobInstanceTable>(
    identityTable = TravakoJobInstanceTable(travakoExposedNamingStrategy),
    database = database
), JobInstanceStore {

    private fun JobInstanceDomain.map() = this as TravakoJobInstance

    override val storeQuery: JobInstanceStoreQuery =
        JobInstanceStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): JobInstanceCreator =
        JobInstanceCreatorImpl(
            identityTable = identityTable,
            database = database
        )

    override fun JobInstanceDomain.identityUpdater(): JobInstanceUpdater =
        JobInstanceUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: JobInstanceDomain) {
        identity as TravakoJobInstance
        this[identityTable.server] = identity.serverUuid
        this[identityTable.jobKey] = identity.jobKey
        this[identityTable.jobClassName] = identity.jobClassName

        this[identityTable.jobTrigger] = identity.jobTrigger
        this[identityTable.jobStatus] = identity.jobStatus
        this[identityTable.jobTriggerType] = identity.jobTriggerType
        this[identityTable.assignedTo] = identity.assignedToUuid
        this[identityTable.lastRunningTime] = identity.lastRunningTime
        this[identityTable.nextExecutionTime] = identity.nextExecutionTime
        this[identityTable.singleRun] = identity.singleRun
    }

    override fun <K : Any> UpdateBuilder<K>.updateEntity(identity: JobInstanceDomain) {
        identity as TravakoJobInstance
        this[identityTable.jobTrigger] = identity.jobTrigger
        this[identityTable.jobStatus] = identity.jobStatus
        this[identityTable.jobTriggerType] = identity.jobTriggerType
        this[identityTable.assignedTo] = identity.assignedToUuid
        this[identityTable.lastRunningTime] = identity.lastRunningTime
        this[identityTable.nextExecutionTime] = identity.nextExecutionTime
        this[identityTable.singleRun] = identity.singleRun
    }

    override fun deleteAllByLastRunningDateAndStatus(
        lastRunningTime: LocalDateTime,
        statuses: List<JobStatus>,
        server: ServerDomain,
    ) {
        ensureInTransaction(database) {
            identityTable.deleteWhere {
                (identityTable.lastRunningTime lessEq lastRunningTime)
                    .and(OrOp(statuses.map { identityTable.jobStatus eq it }))
                    .and(identityTable.server.eq(server.uuid))
            }
        }
    }
}
