package io.arkitik.travako.adapter.exposed.job

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.travako.adapter.exposed.job.creator.JobInstanceCreatorImpl
import io.arkitik.travako.adapter.exposed.job.query.JobInstanceStoreQueryImpl
import io.arkitik.travako.adapter.exposed.job.updater.JobInstanceUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstance
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.JobInstanceStore
import io.arkitik.travako.store.job.creator.JobInstanceCreator
import io.arkitik.travako.store.job.query.JobInstanceStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceUpdater
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 27 7:18 PM, **Mon, December 2021**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
class JobInstanceStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, JobInstanceDomain, TravakoJobInstanceTable>(
    TravakoJobInstanceTable(travakoExposedNamingStrategy)
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
        this[identityTable.jobKey] = identity.jobKey
        this[identityTable.jobClassName] = identity.jobClassName
        this[identityTable.jobStatus] = identity.jobStatus
        this[identityTable.server] = identity.map().server.uuid
        this[identityTable.jobTrigger] = identity.jobTrigger
        this[identityTable.jobTriggerType] = identity.jobTriggerType
        this[identityTable.assignedTo] = identity.map().assignedTo?.uuid
        this[identityTable.lastRunningTime] = identity.lastRunningTime
        this[identityTable.nextExecutionTime] = identity.nextExecutionTime
        this[identityTable.singleRun] = identity.singleRun
    }
}
