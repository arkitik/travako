package io.arkitik.travako.adapter.exposed.job

import io.arkitik.radix.adapter.exposed.ExposedStore
import io.arkitik.radix.develop.exposed.table.ensureInTransaction
import io.arkitik.travako.adapter.exposed.job.creator.JobInstanceParamCreatorImpl
import io.arkitik.travako.adapter.exposed.job.query.JobInstanceParamStoreQueryImpl
import io.arkitik.travako.adapter.exposed.job.updater.JobInstanceParamUpdaterImpl
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.core.domain.job.embedded.JobStatus
import io.arkitik.travako.core.domain.server.ServerDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceParam
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceParamTable
import io.arkitik.travako.protocol.naming.strategy.TravakoExposedNamingStrategy
import io.arkitik.travako.store.job.JobInstanceParamStore
import io.arkitik.travako.store.job.creator.JobInstanceParamCreator
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery
import io.arkitik.travako.store.job.updater.JobInstanceParamUpdater
import org.jetbrains.exposed.v1.core.OrOp
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.exists
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.stringLiteral
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import java.time.LocalDateTime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:20 PM, 26/08/2024
 */
class JobInstanceParamStoreImpl(
    database: Database?,
    travakoExposedNamingStrategy: TravakoExposedNamingStrategy,
) : ExposedStore<String, JobInstanceParamDomain, TravakoJobInstanceParamTable>(
    identityTable = TravakoJobInstanceParamTable(travakoExposedNamingStrategy),
    database = database
), JobInstanceParamStore {
    private fun JobInstanceParamDomain.map() = this as TravakoJobInstanceParam

    override val storeQuery: JobInstanceParamStoreQuery =
        JobInstanceParamStoreQueryImpl(
            database = database,
            identityTable = identityTable,
        )

    override fun identityCreator(): JobInstanceParamCreator =
        JobInstanceParamCreatorImpl(
            identityTable = identityTable,
            database = database
        )

    override fun JobInstanceParamDomain.identityUpdater(): JobInstanceParamUpdater =
        JobInstanceParamUpdaterImpl(map())

    override fun <K : Any> UpdateBuilder<K>.createEntity(identity: JobInstanceParamDomain) {
        this[identityTable.key] = identity.key
        this[identityTable.value] = identity.value
        this[identityTable.job] = identity.map().jobUuid
    }

    override fun deleteAllParamsForJobsByLastRunningDateAndStatus(
        lastRunningTime: LocalDateTime,
        statuses: List<JobStatus>,
        server: ServerDomain,
    ) {
        ensureInTransaction(database) {
            val jobInstanceTable = identityTable.jobInstanceTable
            identityTable
                .deleteWhere {
                    exists(
                        jobInstanceTable
                            .select(stringLiteral("1"))
                            .where {
                                (jobInstanceTable.uuid.eq(identityTable.job))
                                    .and(jobInstanceTable.server.eq(server.uuid))
                                    .and(jobInstanceTable.lastRunningTime.lessEq(lastRunningTime))
                                    .and(OrOp(statuses.map { jobInstanceTable.jobStatus eq it }))
                            }
                    )
                }
        }
    }
}
