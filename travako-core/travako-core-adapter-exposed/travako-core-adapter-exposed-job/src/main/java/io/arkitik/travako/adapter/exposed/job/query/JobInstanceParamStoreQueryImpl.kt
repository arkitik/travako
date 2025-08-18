package io.arkitik.travako.adapter.exposed.job.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.travako.core.domain.job.JobInstanceDomain
import io.arkitik.travako.core.domain.job.JobInstanceParamDomain
import io.arkitik.travako.entity.exposed.job.TravakoJobInstanceParamTable
import io.arkitik.travako.store.job.query.JobInstanceParamStoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:21 PM, 26/08/2024
 */
internal class JobInstanceParamStoreQueryImpl(
    database: Database?,
    identityTable: TravakoJobInstanceParamTable,
) : ExposedStoreQuery<String, JobInstanceParamDomain, TravakoJobInstanceParamTable>(
    identityTable = identityTable,
    database = database
), JobInstanceParamStoreQuery {
    override fun findAllByJobInstance(jobInstance: JobInstanceDomain): List<JobInstanceParamDomain> {
        return transaction(database) {
            identityTable.selectAll()
                .where { identityTable.job eq jobInstance.uuid }
                .map { identityTable.mapToIdentity(it, database) }
        }
    }
}
