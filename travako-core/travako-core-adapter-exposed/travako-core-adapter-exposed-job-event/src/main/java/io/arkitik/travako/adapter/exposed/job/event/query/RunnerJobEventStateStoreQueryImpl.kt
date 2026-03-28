package io.arkitik.travako.adapter.exposed.job.event.query

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.travako.core.domain.runner.SchedulerRunnerDomain
import io.arkitik.travako.domain.job.event.JobEventDomain
import io.arkitik.travako.domain.job.event.RunnerJobEventStateDomain
import io.arkitik.travako.entity.exposed.job.event.TravakoRunnerJobEventStateTable
import io.arkitik.travako.store.job.event.query.RunnerJobEventStateStoreQuery
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 04 5:10 PM, **Tue, January 2022**
 * Project *travako* [arkitik.io](https://arkitik.io)
 */
internal class RunnerJobEventStateStoreQueryImpl(
    database: Database?,
    identityTable: TravakoRunnerJobEventStateTable,
) : ExposedStoreQuery<String, RunnerJobEventStateDomain, TravakoRunnerJobEventStateTable>(
    database = database,
    identityTable = identityTable,
), RunnerJobEventStateStoreQuery {
    override fun existsByRunnerAndEvent(
        schedulerRunner: SchedulerRunnerDomain,
        jobEvent: JobEventDomain,
    ): Boolean =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    (identityTable.runner eq schedulerRunner.uuid).and(identityTable.jobEvent eq jobEvent.uuid)
                }.exist()
        }

    override fun countByEvent(jobEvent: JobEventDomain): Long =
        transaction(database) {
            identityTable.selectAll()
                .where {
                    identityTable.jobEvent eq jobEvent.uuid
                }.count()
        }
}
